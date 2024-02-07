package com.milkfoam.mcgocqhttp.service.websocket

import com.milkfoam.mcgocqhttp.service.*
import okhttp3.WebSocketListener

// 定义 Lambda 函数类型
typealias OnOpenHandler = (okhttp3.WebSocket, okhttp3.Response) -> Unit
typealias OnMessageHandler = (okhttp3.WebSocket, String) -> Unit
typealias OnClosingHandler = (okhttp3.WebSocket, Int, String) -> Unit
typealias OnClosedHandler = (okhttp3.WebSocket, Int, String) -> Unit
typealias OnFailureHandler = (okhttp3.WebSocket, Throwable, okhttp3.Response?) -> Unit

class WebSocketListenerAdapter : WebSocketListener() {
    var onOpenHandler: OnOpenHandler? = null
    var onMessageHandler: OnMessageHandler? = null
    var onClosingHandler: OnClosingHandler? = null
    var onClosedHandler: OnClosedHandler? = null
    var onFailureHandler: OnFailureHandler? = null

    override fun onOpen(webSocket: okhttp3.WebSocket, response: okhttp3.Response) {
        onOpenHandler?.invoke(webSocket, response)
    }

    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
        onMessageHandler?.invoke(webSocket, text)
    }

    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        onClosingHandler?.invoke(webSocket, code, reason)
    }

    override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        onClosedHandler?.invoke(webSocket, code, reason)
    }

    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: okhttp3.Response?) {
        onFailureHandler?.invoke(webSocket, t, response)
    }
}