package com.example.my_heaven.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.model.restapi.base.CreateSearch


class SearchAdapter (private val searchlist : List<CreateSearch>, private val context: Context)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_item_search, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(searchlist[position],context)

    }

    override fun getItemCount(): Int {
        return searchlist.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val search_place = itemView?.findViewById<TextView>(R.id.tv_search_place)
        val search_re_name = itemView?.findViewById<TextView>(R.id.tv_search_resident_name)
        val search_de_name = itemView?.findViewById<TextView>(R.id.tv_search_de_name)
        val cl_search = itemView?.findViewById<ConstraintLayout>(R.id.cl_search)

        fun bind(list: CreateSearch?, context: Context) {
            search_place?.text = list?.place
            search_re_name?.text = list?.resident
            search_de_name?.text = list?.deceased
        }
    }
}