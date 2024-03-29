package com.lui2mi.logcatmonitor.utils

import com.lui2mi.logcatmonitor.MainService
import com.lui2mi.logcatmonitor.models.Subscription
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WSListener(val group: String, val bind: MainService.Bind): WebSocketListener() {
    var connected = false
    var status = ""
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        status = "Closed"
        bind.callWs(status)
        connected = false
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        status = "Closing"
        bind.callWs(status)
        connected = false

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        status = "Failure"
        bind.callWs(status)
        connected = false
        bind.reconnectWebsocket()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        connected = true
        status = "Connected"
        bind.callWs(status)
        val event = com.lui2mi.logcatmonitor.models.Event("subscribe", Subscription(group))
        webSocket.send(event.toString())
    }
}