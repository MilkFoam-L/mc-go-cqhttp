package com.milkfoam.mcgocqhttp.service.websocket

import com.milkfoam.mcgocqhttp.McGoCqHttp
import com.milkfoam.mcgocqhttp.McGoCqHttp.configData
import com.milkfoam.mcgocqhttp.utils.consoleErrorMsg
import com.milkfoam.mcgocqhttp.utils.consoleInfoMsg
import com.milkfoam.mcgocqhttp.utils.debugInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.ws.RealWebSocket
import java.util.concurrent.TimeUnit


private var client: OkHttpClient? = null
private var webSocket: RealWebSocket? = null

class CustomWebSocket(url: String?, handler: WebSocketListenerAdapter.() -> Unit) {

    init {
        if (webSocket != null) {
            webSocket?.close(1001, "已有连接,关闭")
            consoleInfoMsg("缓存内已有WebSocket连接池,自动关闭")
        }
        if (url == null) {
            error("WebSocket连接地址为空")
        }
        runCatching {
            client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
                .readTimeout(60, TimeUnit.SECONDS)          //设置读超时
                .writeTimeout(60, TimeUnit.SECONDS)         //设置写超时
                .retryOnConnectionFailure(true)
                .build()

            val enableToken = configData!!.webSocketConfig.accessToken
            val tokenValue = configData!!.webSocketConfig.accessTokenValue
            debugInfo("enableToken: $enableToken")
            debugInfo("tokenValue: $tokenValue")
            val request: Request = if (enableToken) {
                Request.Builder()
                    .header("Authorization", "Bearer $tokenValue")
                    .url(url)
                    .build()
            } else {
                Request.Builder()
                    .url(url)
                    .build()
            }

            val listener = WebSocketListenerAdapter().apply(handler)
            webSocket = client?.newWebSocket(request, listener) as RealWebSocket
        }.onFailure {
            it.printStackTrace()
        }

    }

    fun send(message: String) {
        val isMessageSent = webSocket?.send(message) ?: false
        if (!isMessageSent) {
            consoleErrorMsg("消息发送失败：WebSocket 连接未建立或已关闭")
        }
    }

    fun close() {
        webSocket?.close(1001, null)
    }

}