package com.example.my_heaven.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.model.list.Obituaray
import com.example.my_heaven.util.`object`.Constant.BURIED
import com.example.my_heaven.util.`object`.Constant.COFFIN_DATE
import com.example.my_heaven.util.`object`.Constant.DECEASED_NAME
import com.example.my_heaven.util.`object`.Constant.DOFP_DATE
import com.example.my_heaven.util.`object`.Constant.EOD_DATE
import com.example.my_heaven.util.`object`.Constant.PLACE_NAME
import com.example.my_heaven.util.`object`.Constant.RESIDENT_NAME
import com.example.my_heaven.view.side.list.ListDetailActivity

class RecyclerAdapter(val postList : List<Obituaray>, val context : Context)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position],context)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(it,position)
        }
    }

    interface OnItemClickListener{
        fun onClick(v:View,position: Int)
    }

    private var itemClickListener: OnItemClickListener?=null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    override fun getItemCount(): Int {
        return postList.count()
    }


    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val tx_date = itemView?.findViewById<TextView>(R.id.list_tx_date)
        val tx_top_name = itemView?.findViewById<TextView>(R.id.tx_top_name)
        val tx_body_name  = itemView?.findViewById<TextView>(R.id.tx_body_name)
        val tx_body_info =   itemView?.findViewById<TextView>(R.id.tx_body_info)
        val btn_show = itemView?.findViewById<Button>(R.id.btn_list_show)

        fun bind(itemPhoto : Obituaray? , context: Context){
            tx_date?.text = itemPhoto?.eod?.date
            tx_top_name?.text = itemPhoto?.deceased?.name
            tx_body_name?.text = itemPhoto?.resident?.name
            tx_body_info?.text=itemPhoto?.place?.place_name

            btn_show?.setOnClickListener {
                val intent = Intent(context, ListDetailActivity::class.java)
                intent.putExtra(DECEASED_NAME,itemPhoto?.deceased?.name.toString())
                intent.putExtra(EOD_DATE,itemPhoto?.eod?.date.toString())
                intent.putExtra(RESIDENT_NAME,itemPhoto?.resident?.name.toString())
                intent.putExtra(PLACE_NAME,itemPhoto?.place?.place_name.toString())
                intent.putExtra(COFFIN_DATE,itemPhoto?.coffin?.date.toString())
                intent.putExtra(DOFP_DATE,itemPhoto?.dofp?.date.toString())
                intent.putExtra(BURIED,itemPhoto?.buried.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }

    }
}