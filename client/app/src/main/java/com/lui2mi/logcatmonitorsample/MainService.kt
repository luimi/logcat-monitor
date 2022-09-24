package com.lui2mi.logcatmonitorsample


import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.jaredrummler.ktsh.Shell
import com.lui2mi.logcatmonitorsample.models.Event
import com.lui2mi.logcatmonitorsample.models.Message
import com.lui2mi.logcatmonitorsample.utils.General
import com.lui2mi.logcatmonitorsample.utils.WSListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainService: Service() {
    private val binder = Bind()
    lateinit var ws: WebSocket
    lateinit var wsListener: WSListener
    lateinit var shell: Shell
    lateinit var code: String
    lateinit var server: String
    val stdOutLineListener = object : Shell.OnLineListener{
        override fun onLine(line: String) {
            binder.callLog(line)
            ws.send(Event("send", Message(code!!,line)).toString())
        }
    }
    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        server = General.getSharedPreferences(this).getString("server","")!!
        code = General.getSharedPreferences(this).getString("code","NTFD")!!
        if(server.isEmpty() || server.isBlank() || code.isEmpty() || code.isBlank()){

        } else {
            startLog()
            startWebsocket()
        }
        return START_NOT_STICKY
    }
    fun startLog(){
        Thread {
            shell = Shell("sh")
            shell.addOnStdoutLineListener(stdOutLineListener)
            shell.run("su")
            shell.run("logcat -c")
            shell.run("logcat")
        }.start()
    }
    fun startWebsocket(){
        binder.callWs("Connecting")
        val client = OkHttpClient()
        val request = Request.Builder().url(server).build()
        wsListener = WSListener(code, binder)
        ws = client.newWebSocket(request,wsListener)
    }
    override fun onDestroy() {
        shell.interrupt()
        shell.removeOnStdoutLineListener(stdOutLineListener)
        if(wsListener.connected){
            ws.close(1000,"End")
        }
        super.onDestroy()
    }

    inner class Bind: Binder() {
        lateinit var logCallback: (log: String) -> Unit
        lateinit var wsCallback: (status: String) -> Unit

        fun isLogCallback(): Boolean{
            return this::logCallback.isInitialized
        }
        fun isWsCallback(): Boolean{
            return this::wsCallback.isInitialized
        }
        fun callLog(log: String){
            if(isLogCallback()){
                logCallback(log)
            }
        }
        fun callWs(status: String){
            if(isWsCallback()){
                wsCallback(status)
            }
        }
        fun reconnectWebsocket(){
            Handler(Looper.getMainLooper()).postDelayed({
                startWebsocket()
            },10000)
        }
    }
}