package com.example.my_heaven.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.model.notice.Announcement


class NoticeAdapter(private val noticeList : List<Announcement>, val context: Context)
    : RecyclerView.Adapter<NoticeAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_notice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeAdapter.ViewHolder, position: Int) {
        holder.bind(noticeList[position],context)
    }

    override fun getItemCount(): Int {
        return noticeList.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val notice_title = itemView?.findViewById<TextView>(R.id.list_tx_date)
        val notice_timestamp = itemView?.findViewById<TextView>(R.id.tx_top_name)

        fun bind(itemPhoto : Announcement?, context: Context){
            notice_title?.text = itemPhoto?.title.toString()
            notice_timestamp?.text = itemPhoto?.time.toString()
        }
    }
}