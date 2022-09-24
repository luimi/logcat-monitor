package com.lui2mi.logcatmonitorsample.models


class Log(val data: String) {

    val logcatMessage = "^\\d+-\\d+\\s+\\d+:\\d+:\\d+.\\d+\\s+\\d+\\s+\\d+\\s+[A-Z]\\s".toRegex()
    val logcatTitle = "^[\\S\\s]*?:".toRegex()
    val logcatSpaces = "\\s+".toRegex()

    var date: String = ""
    var time: String = ""
    var process: String = ""
    var threadid: String = ""
    var type: String = ""
    var message: String = ""
    var title: String = ""
    init {
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
    }
    companion object {
        val logcat = "^\\d+-\\d+\\s+\\d+:\\d+:\\d+.\\d+\\s+\\d+\\s+\\d+\\s+[A-Z]\\s[\\s\\S]+".toRegex()
        fun isLog(log: String): Boolean{
            return logcat.matches(log)
        }
    }
}