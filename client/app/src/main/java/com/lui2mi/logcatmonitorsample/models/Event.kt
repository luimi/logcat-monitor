package com.lui2mi.logcatmonitorsample.models

import com.google.gson.Gson

class Event<T>(val event:String, val data: T){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}