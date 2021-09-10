package com.lui2mi.logcatmonitor.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lui2mi.logcatmonitor.R

class LogAdapter(val context: Context): RecyclerView.Adapter<LogAdapter.LogHolder>() {
    var logs: ArrayList<String> = arrayListOf()
    class LogHolder(val v: View): RecyclerView.ViewHolder(v){
        val log: TextView = v.findViewById(R.id.tv_log)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_log,null)
        return LogHolder(v)
    }

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        holder.log.setText(logs[position])
    }

    override fun getItemCount(): Int = logs.size
}