package com.milkfoam.mcgocqhttp.service.request

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.to
import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.data.SenderData
import com.milkfoam.mcgocqhttp.service.websocket.CustomWebSocket
import com.milkfoam.mcgocqhttp.utils.debugInfo
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync

class GetUserInfo {

    fun getData(userId: String): SenderData? {

        var data: SenderData? = null
        submitAsync {
            val formattedMessage =
                """
            {
                "action": "get_stranger_info",
                "params": {
                  "user_id": "$userId",
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
                    data = jsonObject.to<SenderData>("data")
                }
            }.apply {
                submit(delay = 50) {
                    close()
                    debugInfo("GetUserInfo: Websocket已自动关闭")
                }
            }

            debugInfo("GetUserInfo: $data")
        }
        return data

    }


}