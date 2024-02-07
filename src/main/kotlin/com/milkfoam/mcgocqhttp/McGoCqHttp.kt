package com.milkfoam.mcgocqhttp

import com.alibaba.fastjson2.parseObject
import com.milkfoam.mcgocqhttp.api.event.QQMessageEvent
import com.milkfoam.mcgocqhttp.api.event.UserJoinGroupEvent
import com.milkfoam.mcgocqhttp.api.event.UserQuitGroupEvent
import com.milkfoam.mcgocqhttp.data.ConfigurationData
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.io.ConfigurationReader
import com.milkfoam.mcgocqhttp.rcon.Rcon
import com.milkfoam.mcgocqhttp.service.websocket.CustomWebSocket
import com.milkfoam.mcgocqhttp.update.Updater.grabInfo
import com.milkfoam.mcgocqhttp.utils.*
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.pluginVersion
import taboolib.platform.util.bukkitPlugin


object McGoCqHttp : Plugin() {

    var configData: ConfigurationData? = null

    var webSocketInstance: CustomWebSocket? = null

    var rcon: Rcon? = null
    override fun onEnable() {
        prefixInfo("                                                  _     _   _         ")
        prefixInfo("  _ __ ___   ___       __ _  ___         ___ __ _| |__ | |_| |_ _ __  ")
        prefixInfo(" | '_ ` _ \\ / __|____ / _` |/ _ \\ _____ / __/ _` | '_ \\| __| __| '_ \\ ")
        prefixInfo(" | | | | | | (_______| (_| | (_) |_____| (__ (_| | | | | |_| |_| |_) |")
        prefixInfo(" |_| |_| |_|\\___|     \\__, |\\___/       \\___\\__, |_| |_|\\__|\\__| .__/ ")
        prefixInfo("                      |___/                    |_|             |_|    ")
        prefixInfo("                                                                      ")
        prefixInfo("        &a mc-go-cqhttp  &bv${pluginVersion}  &6by Milk_Foam  ")
        prefixInfo("        &a Supported Versions : &b1.8 - 1.20.4 ")
        prefixInfo("        &a Running on &b${bukkitPlugin.server.version} ")
        // 读取配置文件
        readConfig()
    }

    override fun onActive() {
        // 开始连接机器人
        runBot()
        // 自动更新
        grabInfo()
        // 连接Rcon
        connectRcon()
    }

    override fun onDisable() {
        webSocketInstance?.close()
    }

    // 加载配置文件
    fun readConfig() {
        configData = ConfigurationReader.readModule()
    }

    // 连接RCON
    fun connectRcon() {
        if (configData?.rconEnable == true) {
            kotlin.runCatching {
                rcon = Rcon(
                    configData?.rconHost,
                    configData?.rconPort ?: 8080,
                    configData?.rconPassword?.toByteArray()
                )
            }.onFailure {
                consoleErrorMsg("&c Rcon 连接失败!")
                consoleErrorMsg("以下内容为报错: $it")
            }.onSuccess {
                consoleInfoMsg("&a Rcon 已连接!")
            }
        } else {
            consoleErrorMsg("&c Rcon 未启用!")
        }
    }

    // 连接机器人
    fun runBot() {
        webSocketInstance = CustomWebSocket(configData?.webSocketConfig?.webSocket) {
            onOpenHandler = { _, _ ->
                // 处理打开连接
                consoleInfoMsg("&a go-cqhttp 连接成功!")
            }
            onMessageHandler = { _, text ->
                debugInfo("&2onMessageHandler: $text")
                // 处理消息
                val postData = text.parseObject<PostData>()
                // 判断消息类型
                when (postData.postType) {
                    "message" -> {
                        // 触发消息事件
                        QQMessageEvent(text, postData).call()
                    }

                    "request" -> {
                        if (postData.requestType == "group") {
                            // 触发入群事件
                            postData.userId.withNotNull {
                                UserJoinGroupEvent(postData).call()
                            }
                        }
                    }

                    "notice" -> {
                        if (postData.noticeType == "group_decrease") {
                            // 触发退群事件
                            postData.userId.withNotNull {
                                UserQuitGroupEvent(postData).call()
                            }
                        }
                    }
                }

            }
            onClosedHandler = { _, _, _ ->
                // 处理连接已关闭
                consoleInfoMsg("&a go-cqhttp 断开连接!")
                if (configData?.autoReconnect == true){
                    consoleInfoMsg("&a 自动重连已启动...")
                    consoleInfoMsg("&a 正在尝试重新连接...")
                    runBot()
                }
            }
            onFailureHandler = { _, t, _ ->
                // 处理失败
                consoleInfoMsg("&c go-cqhttp 出现错误!")
                consoleInfoMsg("以下内容为报错: $t")
            }
        }

    }
}