package com.lui2mi.logcatmonitor.models

import android.util.Log
import java.util.*


class Log(val data: String) {
    var date: String = ""
    var time: String = ""
    var process: String = ""
    var threadid: String = ""
    var type: String = ""
    var message: String = ""
    var title: String = ""
    var isLog: Boolean = false
    init {
        if(checkA5Logcat()) getA5Logcat()
        else if(checkA6Logcat()) getA6Logcat()
    }
    fun checkA5Logcat(): Boolean{
        val logcat = "^[VDIEW]\\/[\\w.\\s]+\\([\\s\\S]\\d*\\):[\\s\\S]+".toRegex()
        return logcat.matches(data)
    }
    fun checkA6Logcat(): Boolean {
        val logcat = "^\\d+-\\d+\\s+\\d+:\\d+:\\d+.\\d+\\s+\\d+\\s+\\d+\\s+[A-Z]\\s[\\s\\S]+".toRegex()
        return logcat.matches(data)
    }
    fun getA5Logcat(){
        val _type = "^[VDIEW]/".toRegex()
        val _title = "/[\\w.\\s]+\\(".toRegex()
        val _process = "\\([\\s\\S]\\d*\\)".toRegex()
        val _message = ":[\\s\\S]+".toRegex()
        val cd = Date()
        type = _type.find(data)?.value!!.replace("/","")
        title = _title.find(data)?.value!!.replace(Regex("[/\\(]"),"")
        process = _process.find(data)?.value!!.replace(Regex("[\\(\\s\\)]"),"")
        threadid = _process.find(data)?.value!!.replace(Regex("[\\(\\s\\)]"),"")
        message = _message.find(data)?.value!!.replace(Regex("^:\\s"),"")
        date = "${cd.month+1}-${cd.date}"
        time = "${cd.hours}:${cd.minutes}:${cd.seconds}"
        isLog = true
    }
    fun getA6Logcat(){
        val logcatMessage = "^\\d+-\\d+\\s+\\d+:\\d+:\\d+.\\d+\\s+\\d+\\s+\\d+\\s+[A-Z]\\s".toRegex()
        val logcatTitle = "^[\\S\\s]*?:".toRegex()
        val logcatSpaces = "\\s+".toRegex()
        val body = data.replace(logcatMessage, "")
        message = body.replace(logcatTitle, "")
        title = body.replace(message, "")
        var header = data.replace(body, "")
        var _header = header.split(logcatSpaces)
        date = _header[0]
        time = _header[1]
        process = _header[2]
        threadid = _header[3]
        type = _header[4]
        isLog = true
    }
}