package com.milkfoam.mcgocqhttp.service.request

import com.milkfoam.mcgocqhttp.McGoCqHttp.webSocketInstance
import taboolib.common.platform.function.submitAsync

class GroupMessage {

    //发送群聊消息
    fun send(groupId: String, message: String) {
        submitAsync {
            val formattedMessage =
                """
            {
                "action": "send_group_msg",
                "params": {
                  "group_id": "$groupId",
                  "message": "$message"
                }
            }
            """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }


}