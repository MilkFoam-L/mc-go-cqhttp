package com.milkfoam.mcgocqhttp.module

import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.McGoCqHttp.rcon
import com.milkfoam.mcgocqhttp.api.MGCAPI
import com.milkfoam.mcgocqhttp.api.event.QQMessageEvent
import com.milkfoam.mcgocqhttp.data.PostData
import com.milkfoam.mcgocqhttp.rcon.Rcon
import com.milkfoam.mcgocqhttp.utils.consoleErrorMsg
import com.milkfoam.mcgocqhttp.utils.debugInfo
import com.milkfoam.mcgocqhttp.utils.replace
import com.milkfoam.mcgocqhttp.utils.withNotNull
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.uncolored

object ConsoleTool {


    @SubscribeEvent
    fun startListening(e: QQMessageEvent) {
        // 获取消息返回
        val postData: PostData = e.data
        // 获取配置
        val data = configData?.consoleTool ?: return debugInfo("控制台工具: 配置文件为空")
        // 判断是否开启
        if (!data.enable) return debugInfo("控制台工具: 未开启")
        // 获取消息
        val message = postData.rawMessage
        // 获取用户ID
        val userId = postData.userId
        // 获取关键词
        val key = data.keyword
        // 获取管理员列表
        val admins = data.admins
        // 判断是否为空
        if (message == null || userId == null) return debugInfo("控制台工具: 消息或用户ID为空")
        // 判断是否为管理员
        if (!admins.contains(userId)) return debugInfo("控制台工具: 用户不是管理员")
        // 判断是否为关键词
        if (!message.startsWith(key)) return debugInfo("控制台工具: 消息不包含关键词")
        // 获取命令
        val cmd = message.replace(key)
        // 获取返回值
        val result = rcon?.command(cmd)?.uncolored() ?: return debugInfo("控制台工具: Rcon为空")
        // 发送返回值
        when (postData.messageType) {
            "group" -> {
                postData.groupId.withNotNull {
                    MGCAPI.sendGroupMessage(it, result)
                }
            }

            "private" -> {
                postData.userId.let {
                    MGCAPI.sendPrivateMessage(it, result)
                }
            }
        }

    }


}