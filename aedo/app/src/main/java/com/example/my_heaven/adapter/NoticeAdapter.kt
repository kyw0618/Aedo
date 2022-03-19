package com.example.my_heaven.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.model.notice.Announcement


class NoticeAdapter(private val noticeList : List<Announcement>,private val context: Context)
    : RecyclerView.Adapter<NoticeAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_item_notice, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: NoticeAdapter.ViewHolder, position: Int) {
        holder.bind(noticeList[position],context)

    }

    override fun getItemCount(): Int {
        return noticeList.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val notice_title = itemView?.findViewById<TextView>(R.id.tv_counseling_title)
        val notice_timestamp = itemView?.findViewById<TextView>(R.id.tv_counseling_timestamp)

        fun bind(list: Announcement?,context: Context) {
            notice_title?.text = list?.title
            notice_timestamp?.text = list?.created
        }
    }
}