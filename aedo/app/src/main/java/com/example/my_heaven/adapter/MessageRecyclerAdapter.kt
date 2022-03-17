package com.example.my_heaven.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.model.list.Condole
import com.example.my_heaven.model.list.CondoleList

class MessageRecyclerAdapter(private val messageList : List<CondoleList>, val context: Context) :
    RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position],context)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(it,position)
        }
    }

    interface OnItemClickListener{
        fun onClick(v:View,position: Int)
    }

    private var itemClickListener: OnItemClickListener?=null

    override fun getItemCount(): Int {
        return messageList.count()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val message_title : TextView = itemView.findViewById(R.id.tv_message_title)
        private val message_name : TextView = itemView.findViewById(R.id.tv_message_name)
        private val message_time : TextView = itemView.findViewById(R.id.tv_message_time)

        fun bind(item: CondoleList, context: Context) {
            message_title.text = item.title.toString()
            message_name.text = item.obld.toString()
            message_time.text = item.created.toString()
        }
    }
}