package com.lui2mi.logcatmonitor.utils

import android.util.Log
import com.jaredrummler.ktsh.Shell
import com.lui2mi.logcatmonitor.MainService
import com.lui2mi.logcatmonitor.models.Event
import com.lui2mi.logcatmonitor.models.Message
import com.lui2mi.logcatmonitor.models.Subscription
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

class WSListener(val group: String, val bind: MainService.Bind): WebSocketListener() {
    var connected = false
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        bind.callWs("Closed")
        connected = false
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        bind.callWs("Closing")
        connected = false

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        bind.callWs("Failure")
        connected = false
        bind.reconnectWebsocket()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        connected = true
        bind.callWs("Connected")
        val event = Event("subscribe",Subscription(group))
        webSocket.send(event.toString())
    }
}