package com.lui2mi.logcatmonitorsample

import android.content.ComponentName
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lui2mi.logcatmonitor.MainService
import com.lui2mi.logcatmonitor.models.Log
import com.lui2mi.logcatmonitor.models.LogCatMonitor
import com.lui2mi.logcatmonitorsample.utils.LogAdapter
import com.lui2mi.logcatmonitorsample.utils.Utils


class MainActivity : AppCompatActivity() {
    lateinit var server: EditText
    lateinit var code: String
    lateinit var service: TextView
    lateinit var websocket: TextView
    lateinit var start_stop: Button
    lateinit var update: Button
    lateinit var log: Button
    lateinit var logs: RecyclerView
    lateinit var autoscroll: CheckBox
    lateinit var bind: MainService.Bind
    lateinit var logCatMonitor: LogCatMonitor
    var counter = 0

    val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            bind = p1 as MainService.Bind
            bind.logCallback = {
                val log = Log(it)
                if(log.isLog){
                    val adapter = logs.adapter as LogAdapter
                    adapter.logs.add(log)
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                        if(autoscroll.isChecked)
                            logs.scrollToPosition(adapter.logs.size-1)
                    }
                }
            }
            bind.wsCallback = {
                runOnUiThread {
                    websocket.setText("${it}")
                }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        server = findViewById(R.id.edt_server)
        start_stop = findViewById(R.id.btn_start_stop)
        update = findViewById(R.id.btn_update)
        logs = findViewById(R.id.rv_logs)
        service = findViewById(R.id.tv_service)
        websocket = findViewById(R.id.tv_websocket)
        autoscroll = findViewById(R.id.cb_autoscroll)
        log = findViewById(R.id.btn_log)
        // getting id code
        server.setText(Utils.getString(this,"server"))
        if(Utils.hasString(this,"code")){
            code = Utils.getString(this,"code")
        } else {
            code = Utils.getId()
            Utils.setString(this,"code",code)
        }
        // start and stop button
        start_stop.setOnClickListener {
            if(server.text.isBlank() || server.text.isEmpty()){
                return@setOnClickListener
            }
            if(!com.lui2mi.logcatmonitor.utils.Utils.isServiceRunning(this)){
                logCatMonitor = LogCatMonitor(this)
                    .putServer(server.text.toString())
                    .putCode(code)
                    .setPing()
                    .setStatusNotification(R.string.app_name)
                    .setExcludeByTag(arrayListOf("NetworkSecurityConfig","zygote","System","View"))
                    .setFilterByTag(arrayListOf("Ping","LogCatMonitor"))
                logCatMonitor.start()
                logCatMonitor.intent.also { intent ->
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE)
                }

            } else {
                unbindService(serviceConnection)
                logCatMonitor.stop()
            }
            changeStatus()
        }
        // change id code manually
        update.setOnClickListener {
            code = Utils.getId()
            Utils.setString(this,"code",code)
            onResume()
        }

        server.addTextChangedListener {
            Utils.setString(this,"server",server.text.toString())
        }
        // initialize logs list
        logs.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = LogAdapter(this@MainActivity)
        }

        // clear log list
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            val adapter = logs.adapter as LogAdapter
            adapter.logs = arrayListOf()
            adapter.notifyDataSetChanged()
        }

        // log button
        log.setOnClickListener {
            android.util.Log.v("LogCatMonitor","Log: ${counter++}")
        }
    }
    fun changeStatus(){
        val isRunning = com.lui2mi.logcatmonitor.utils.Utils.isServiceRunning(this)
        start_stop.setText(if(isRunning) "STOP" else "START")
        update.visibility = if(isRunning) View.GONE else View.VISIBLE
        log.visibility = if(isRunning) View.VISIBLE else View.GONE
        server.isEnabled = !isRunning
        onResume()
    }
    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.tv_id).setText(code)
        service.setText(if(com.lui2mi.logcatmonitor.utils.Utils.isServiceRunning(this)) "Running" else "Not running")
    }
}