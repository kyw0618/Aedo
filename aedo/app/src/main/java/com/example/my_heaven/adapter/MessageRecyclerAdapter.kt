package com.example.my_heaven.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.model.restapi.base.Condole

class MessageRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val message_title : TextView = itemView.findViewById(R.id.tv_message_title)
        private val message_name : TextView = itemView.findViewById(R.id.tv_message_name)
        private val message_time : TextView = itemView.findViewById(R.id.tv_message_time)

        fun bind(item: Condole) {
            message_title.text = item.title.toString()
            message_name.text = item.obld.toString()
            message_time.text = item.created.toString()
        }
    }

    var datas = mutableListOf<Condole>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_message_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}