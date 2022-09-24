package com.lui2mi.logcatmonitorsample.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lui2mi.logcatmonitorsample.R
import com.lui2mi.logcatmonitorsample.models.Log

class LogAdapter(val context: Context): RecyclerView.Adapter<LogAdapter.LogHolder>() {
    var logs: ArrayList<Log> = arrayListOf()
    class LogHolder(val v: View): RecyclerView.ViewHolder(v){
        val type: TextView = v.findViewById(R.id.tv_type)
        val datetime: TextView = v.findViewById(R.id.tv_datetime)
        val process: TextView = v.findViewById(R.id.tv_process)
        val threadid: TextView = v.findViewById(R.id.tv_threadid)
        val title: TextView = v.findViewById(R.id.tv_title)
        val message: TextView = v.findViewById(R.id.tv_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_log,null)
        return LogHolder(v)
    }

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        val log: Log = logs[position]
        holder.type.setText(log.type)
        holder.datetime.setText("${log.date} ${log.time}")
        holder.process.setText(log.process)
        holder.threadid.setText(log.threadid)
        holder.title.setText(log.title)
        holder.message.setText(log.message)
        var bg = R.drawable.bg_verbose
        when(log.type){
            "I" -> bg = R.drawable.bg_info
            "E" -> bg = R.drawable.bg_danger
            "V" -> bg = R.drawable.bg_verbose
            "W" -> bg = R.drawable.bg_warning
            "D" -> bg = R.drawable.bg_success
        }
        holder.type.setBackgroundResource(bg)
    }

    override fun getItemCount(): Int = logs.size
}