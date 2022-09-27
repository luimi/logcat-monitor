package com.lui2mi.logcatmonitor.utils

import android.content.Context
import android.app.ActivityManager
import com.lui2mi.logcatmonitor.MainService


class Utils {
    companion object {

        fun isServiceRunning(context: Context): Boolean{
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
                if ((MainService::class.java).getName().equals(service.service.className)) {
                    return true
                }
            }
            return false
        }

    }
}