package com.lui2mi.logcatmonitor.utils

import android.content.Context
import android.content.SharedPreferences
import android.app.Application

import android.app.ActivityManager
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.lui2mi.logcatmonitor.MainService


class General {
    companion object {
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        fun getId(): String{
            return (1..4)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");
        }
        fun getSharedPreferences(context: Context): SharedPreferences{
            return context.getSharedPreferences("localStore",Context.MODE_PRIVATE)
        }
        fun setString(context: Context, key: String ,value: String){
            getSharedPreferences(context).edit().putString(key,value).commit()
        }
        fun getString(context: Context, key: String):String {
            return getSharedPreferences(context).getString(key,"")!!
        }
        fun hasString(context: Context, key: String): Boolean {
            return getSharedPreferences(context).contains(key)
        }
        fun isServiceRunning(context: Context): Boolean{
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
                if ((MainService::class.java).getName().equals(service.service.className)) {
                    return true
                }
            }
            return false
        }
        fun showSimpleAlertDialog(context: Context, message: String){
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.dismiss()
                }
            })
            return builder.create().show()
        }
    }
}