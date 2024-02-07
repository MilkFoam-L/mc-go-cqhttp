package com.milkfoam.mcgocqhttp.service.request

import com.milkfoam.mcgocqhttp.McGoCqHttp
import com.milkfoam.mcgocqhttp.McGoCqHttp.webSocketInstance
import com.milkfoam.mcgocqhttp.data.PostData
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync

class GroupController {

    fun groupBan(groupId: String, userId: String, duration: Int) {
        submitAsync {
            val formattedMessage =
                """
            {
                "action": "set_group_ban",
                "params": {
                  "group_id": "$groupId",
                  "user_id": "$userId",
                  "duration": "$duration"
                }
            }
            """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }

    }

    fun groupAllBan(groupId: String, enable: String) {
        submitAsync {
            val formattedMessage =
                """
            {
                "action": "set_group_ban",
                "params": {
                  "group_id": "$groupId",
                  "enable": "$enable"
                }
            }
            """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }

    fun groupKick(groupId: String, userId: String, rejectAddRequest: String) {
        submit {
            val formattedMessage =
                """
            {
                "action": "set_group_ban",
                "params": {
                  "group_id": "$groupId",
                  "userId": "$userId",
                  "rejectAddRequest": "$rejectAddRequest"
                }
            }
            """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }

    fun setGroupCard(groupId: String, userId: String, card: String) {
        submitAsync {
            val formattedMessage =
                """
            {
                "action": "set_group_card",
                "params": {
                  "group_id": "$groupId",
                  "user_id": "$userId",
                  "card": "$card"
                }
            }
            """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }


    fun acceptJoinGroup(postData: PostData) {
        submitAsync {
            val formattedMessage =
                """
                    {
                        "action": "set_group_add_request",
                        "params": {
                            "flag": "${postData.flag}",
                            "sub_type": "${postData.subType}",
                            "approve": "true"
                         }
                    }
                    """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }

    fun rejectJoinGroup(postData: PostData, reason: String) {
        submitAsync {
            val formattedMessage =
                """
                    {
                        "action": "set_group_add_request",
                        "params": {
                            "flag": "${postData.flag}",
                            "sub_type": "${postData.subType}",
                            "approve": "false",
                            "reason": "$reason"
                         }
                    }
                    """.trimIndent()
            webSocketInstance?.send(formattedMessage)
        }
    }


}