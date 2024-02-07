package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.api.MGCAPI
import com.milkfoam.mcgocqhttp.api.MGCAPI.sendGroupMessage
import com.milkfoam.mcgocqhttp.api.event.QQMessageEvent
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.replace
import com.milkfoam.mcgocqhttp.utils.replaceMessageTag
import org.bukkit.event.player.AsyncPlayerChatEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.submitAsync
import taboolib.module.chat.colored
import taboolib.platform.compat.replacePlaceholder

object MessageConnect {

    /**
     * MC消息转发至QQ
     * @param event AsyncPlayerChatEvent
     */
    @SubscribeEvent
    fun mcToQQ(event: AsyncPlayerChatEvent) {
        // 获取配置文件
        val data = configData?.messageConnect ?: return debugInfo("mcToQQ: 未找到配置文件")
        // 判断是否开启
        if (!data.enable) return debugInfo("mcToQQ: 未开启")

        data.groups.forEach { group ->
            // 判断是否在群组中
            if (!data.groups.contains(group)) return debugInfo("mcToQQ: 不在群组中")
            with(data) {
                //获取玩家
                val player = event.player
                //获取MC原版消息
                val rawMessage = event.message
                //替换原版消息变量和占位符
                val msg = mcToQQText
                    .replace("{player}", player.name)
                    .replace("{message}", rawMessage)
                    .replacePlaceholder(player)
                //判断是否开启关键词功能
                when (mcToQQKeyWordsEnable) {
                    true -> {
                        //判断是否包含关键词
                        if (rawMessage.contains(mcToQQKeyWordsText)) {
                            //发送至群聊列表
                            val replaceMsg =
                                msg.replace(mcToQQKeyWordsText)
                            sendGroupMessage(group, replaceMsg)
                        }
                    }

                    false -> {
                        //发送至群聊列表
                        sendGroupMessage(group, msg)
                    }
                }
            }
        }

    }

    /**
     * QQ消息转发至MC
     * @param e QQMessageEvent
     */
    @SubscribeEvent
    fun qqToMC(e: QQMessageEvent) {

        // 获取配置文件
        val data = configData?.messageConnect ?: return debugInfo("qqToMC: 未找到配置文件")

        with(data) {
            // 判断是否开启
            if (!enable) return debugInfo("qqToMC: 未开启")
            // 判断是否为预设群聊
            if (e.data.messageType != "group") return debugInfo("qqToMC: 不在群组中")
            // 获取群组
            val groupId = e.data.groupId
            // 获取用户ID
            val userId = e.data.userId
            // 获取消息
            var message = e.data.rawMessage
            // 判断是否为空
            if (userId == null || message == null) return debugInfo("qqToMC: 未找到消息或玩家")
            // 判断是否包含cq码，有则进行类型替换
            if (message.contains("CQ")) {
                message = replaceMessageTag(message)
            }
            // 判断是否为控制台指令
            val consoleToolKeyword =
                configData?.consoleTool?.keyword ?: return debugInfo("qqToMC: 未找到consoleToolKeyword配置文件")
            if (message.contains(consoleToolKeyword)) {
                return debugInfo("qqToMC: 消息中包含控制台指令")
            }
            // 获取玩家信息
            val userData = e.data.sender
            // 获取发送玩家名字
            val senderName = if (userData?.card != null) {
                userData.card
            } else {
                userData!!.nickname ?: return debugInfo("qqToMC: 未找到玩家名字")
            }
            debugInfo("qqToMC的userData: $userData")
            if (groups.contains(groupId)) {
                // 判断是否开启关键词功能
                when (qQtoMcKeyWordsEnable) {
                    true -> {
                        // 判断是否包含关键词
                        if (message.contains(qQtoMcKeyWordsText)) {
                            val replaceMsg = message.replace(qQtoMcKeyWordsText, "")
                            onlinePlayers().forEach { proxyplayer ->
                                proxyplayer.sendMessage(
                                    qQtoMcText
                                        .replace("{sender}", senderName)
                                        .replace("{sender_id}", userId)
                                        .replace("{message}", replaceMsg)
                                        .colored()
                                )
                            }
                        }
                    }

                    false -> {
                        onlinePlayers().forEach { proxyplayer ->
                            proxyplayer.sendMessage(
                                qQtoMcText
                                    .replace("{sender}", senderName)
                                    .replace("{sender_id}", userId)
                                    .replace("{message}", message)
                                    .colored()
                            )
                        }
                    }

                }

            }

        }

    }


}