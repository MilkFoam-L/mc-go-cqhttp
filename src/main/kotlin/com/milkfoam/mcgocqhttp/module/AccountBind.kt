package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.api.MGCAPI
import com.milkfoam.mcgocqhttp.api.event.PlayerBindQQEvent
import com.milkfoam.mcgocqhttp.api.event.QQMessageEvent
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.database.DataBaseManager
import com.milkfoam.mcgocqhttp.io.LanguageReader.bindKey
import com.milkfoam.mcgocqhttp.io.LanguageReader.isBind
import com.milkfoam.mcgocqhttp.io.LanguageReader.isInBind
import com.milkfoam.mcgocqhttp.io.LanguageReader.levelIsNotStandard
import com.milkfoam.mcgocqhttp.io.LanguageReader.notFindPlayer
import com.milkfoam.mcgocqhttp.io.LanguageReader.sendMessageToMinecraft
import com.milkfoam.mcgocqhttp.io.LanguageReader.success
import com.milkfoam.mcgocqhttp.io.LanguageReader.timeOut
import com.milkfoam.mcgocqhttp.service.request.GetUserInfo
import com.milkfoam.mcgocqhttp.service.request.GroupMessage
import com.milkfoam.mcgocqhttp.service.request.PrivateMessage
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.runAction
import com.milkfoam.mcgocqhttp.utils.withNotNull
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.dev
import taboolib.common.platform.function.submitAsync
import taboolib.expansion.submitChain
import taboolib.platform.util.nextChatInTick
import taboolib.platform.util.sendLang
import java.util.*

object AccountBind {

    // 绑定状态缓存
    val cacheInBind: MutableMap<UUID, String> = mutableMapOf()

    @SubscribeEvent
    fun starBind(e: QQMessageEvent) {

        // 获取响应消息
        val data: PostData = e.data
        // 判断配置文件是否为空
        if (configData == null) return debugInfo("绑定: 文件为空")
        // 判断是否开启绑定
        if (configData?.accountBind?.enable == false) return debugInfo("绑定: 未开启")
        // 获取绑定配置
        val bindData = configData?.accountBind ?: return debugInfo("绑定: 配置文件为空")

        // 创建携程
        submitChain {
            // 异步绑定
            async {
                // 获取真实消息
                val message = data.rawMessage
                // 获取发送人QQ
                val sendQQ = data.userId
                // 获取群聊号
                val groupId = data.groupId
                // 判断变量是否为空
                if (message == null || sendQQ == null) return@async debugInfo("绑定: 消息或者QQ号为空")
                // 判断是否为监听的群聊
                if (!bindData.groups.contains(groupId)) return@async debugInfo("绑定: 不是监听的群聊")
                // 判断消息是否包含关键词
                if (!message.contains(bindData.key)) return@async debugInfo("绑定: 消息不包含关键词")
                // 获取发送者的信息
                val userData = data.sender
                // 获取发送者的QQ等级
                val qqLevel = userData?.level ?: 1
                // 获取玩家名字
                val playerName = message.split(" ")[1]
                // 判断QQ等级是否小于设定
                if (qqLevel < bindData.lowestLevel) {
                    // 发送消息给QQ群
                    sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + levelIsNotStandard)
                    // 发送消息给玩家
                    Bukkit.getPlayer(playerName)?.sendLang("Minecraft-levelIsNotStandard")
                    return@async
                }
                // 获取玩家
                Bukkit.getPlayer(playerName).withNotNull { player ->
                    // 判断是否为绑定状态
                    if (cacheInBind.containsValue(sendQQ)) {
                        // QQ账号正在绑定中发送消息给QQ群
                        sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + isInBind)
                        return@async
                    }
                    // 获取绑定状态
                    val bindState = DataBaseManager.getQQ(player.uniqueId)
                    // 判断是否未绑定
                    if (bindState != "未绑定" || DataBaseManager.checkQQ(sendQQ)) {
                        // 发送已经绑定消息给游戏内
                        player.sendLang("Minecraft-isBind")
                        // 发送已经绑定消息给QQ群
                        sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + isBind)
                        return@async
                    }
                    // 添加绑定状态
                    cacheInBind[player.uniqueId] = sendQQ
                    // 发送绑定消息给游戏内
                    player.sendLang("Minecraft-Bind", sendQQ, player.name)
                    // 发送绑定消息给QQ群
                    sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + sendMessageToMinecraft)
                    // 监听玩家输入字符串并且超时后发送超时消息给玩家
                    player.nextChatInTick(bindData.expirationTime * 20, { str ->
                        // 判断玩家游戏内输入的关键词
                        if (str == bindKey) {
                            // 删除绑定状态
                            cacheInBind.remove(player.uniqueId, sendQQ)
                            // 储存绑定数据进数据库
                            DataBaseManager.saveQQ(player.uniqueId, sendQQ)
                            // 发送绑定成功消息给游戏内
                            player.sendLang("Minecraft-Success")
                            // 发送绑定成功消息给QQ群
                            sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + success)
                            // 绑定成功执行指令
                            bindData.actions.forEach { runAction(it, player) }
                            // 注册事件
                            PlayerBindQQEvent(sendQQ, player.name, data).call()
                        }
                    }, {
                        // 发送超时消息给游戏内
                        player.sendLang("Minecraft-TimeOut")
                        // 发送超时消息给QQ群
                        sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + timeOut)
                        // 删除绑定状态
                        cacheInBind.remove(player.uniqueId, sendQQ)
                    })
                } ?: run {
                    // 获取玩家失败发送消息给QQ群
                    sendMessage(sendQQ, groupId, MGCAPI.atQQUser(sendQQ) + notFindPlayer)
                }
            }
        }


    }

    /**
     * 发送消息
     * @param privateId String?
     * @param groupId String?
     * @param message String
     */
    private fun sendMessage(privateId: String?, groupId: String?, message: String) {
        if (groupId != null) {
            GroupMessage().send(groupId, message)
        } else if (privateId != null) {
            PrivateMessage().send(privateId, message)
        }
    }

}