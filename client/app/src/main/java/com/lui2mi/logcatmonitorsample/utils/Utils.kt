package com.lui2mi.logcatmonitorsample.utils

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog

class Utils {
    companion object {
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        fun getId(): String{
            return (1..4)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");
        }
        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("localStore", Context.MODE_PRIVATE)
        }
        fun setString(context: Context, key: String, value: String){
            getSharedPreferences(context).edit().putString(key,value).commit()
        }
        fun getString(context: Context, key: String):String {
            return getSharedPreferences(context).getString(key,"")!!
        }
        fun hasString(context: Context, key: String): Boolean {
            return getSharedPreferences(context).contains(key)
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