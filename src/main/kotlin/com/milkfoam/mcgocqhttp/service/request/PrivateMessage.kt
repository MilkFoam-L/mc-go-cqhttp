package com.milkfoam.mcgocqhttp.service.request

import com.milkfoam.mcgocqhttp.McGoCqHttp.webSocketInstance
import taboolib.common.platform.function.submitAsync

class PrivateMessage {

    //发送好友消息
    fun send(userId: String, message: String) {
        submitAsync {
            val formattedMessage =
                """
            {
                "action": "send_private_msg",
                "params": {
                  "user_id": "$userId",
                  "message": "$message"
                }
            }
            """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }


}