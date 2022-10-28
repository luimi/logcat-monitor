package com.lui2mi.logcatmonitor.models

import android.content.Context
import android.content.Intent
import com.lui2mi.logcatmonitor.MainService

class LogCatMonitor(val context: Context) {
    var intent: Intent

    init {
        intent = Intent(context, MainService::class.java)

    }
    fun putServer(server: String): LogCatMonitor {
        intent.putExtra("server",server)
        return this
    }
    fun putCode(code: String) : LogCatMonitor {
        intent.putExtra("code",code)
        return this
    }
    fun setPing(interval: Long) : LogCatMonitor {
        intent.putExtra("ping",interval)
        return this
    }
    fun setPing() : LogCatMonitor {
        return setPing(30_000)
    }
    fun setFilterByTag(filters: ArrayList<String>): LogCatMonitor {
        intent.putExtra("filter", filters)
        return this
    }
    fun setExcludeByTag(excludes: ArrayList<String>): LogCatMonitor {
        intent.putExtra("exclude", excludes)
        return this
    }
    fun start(){
        context.startService(intent)
    }
    fun stop(){
        context.stopService(intent)
    }
}