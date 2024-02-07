package com.milkfoam.mcgocqhttp.service.request

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.to
import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.data.GroupUserData
import com.milkfoam.mcgocqhttp.service.websocket.CustomWebSocket
import com.milkfoam.mcgocqhttp.utils.debugInfo
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync


class GetGroupInfo {

    fun getGroupUserDataList(groupId: String): List<GroupUserData> {
        val groupMembers = mutableListOf<GroupUserData>()
        submitAsync {
            val formattedMessage =
                """
        {
            "action": "get_group_member_list",
            "params": {
                "group_id": "$groupId"
            }
        }
        """.trimIndent()
            CustomWebSocket(configData?.webSocketConfig?.webSocket) {
                onOpenHandler = { websocket, _ ->
                    // 处理打开连接
                    websocket.send(formattedMessage)
                }
                onMessageHandler = { _, text ->
                    val jsonObject = JSON.parseObject(text)
                    val dataArray = jsonObject.getJSONArray("data")
                    for (i in 0 until dataArray.size) {
                        val memberObject = dataArray.getJSONObject(i)
                        val groupMember = memberObject.toJSONString().to<GroupUserData>()
                        groupMembers.add(groupMember)
                    }
                }
            }.apply {
                submit(delay = 50) {
                    close()
                    debugInfo("GetGroupAllUserInfo: Websocket已自动关闭")
                }
            }
            debugInfo("GetGroupAllUserInfo: $groupMembers")
        }
        return groupMembers
    }


}