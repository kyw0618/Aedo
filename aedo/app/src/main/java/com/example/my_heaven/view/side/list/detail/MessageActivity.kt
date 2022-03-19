package com.example.my_heaven.view.side.list.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_heaven.R
import com.example.my_heaven.adapter.MessageRecyclerAdapter
import com.example.my_heaven.adapter.NoticeAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityMessageBinding
import com.example.my_heaven.model.list.Condole
import com.example.my_heaven.model.list.CondoleList
import com.example.my_heaven.util.`object`.Constant
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.util.log.LLog.TAG
import com.example.my_heaven.view.side.list.ListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMessageBinding
    private lateinit var apiServices: APIService
    private var messageRead: MessageRecyclerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        mBinding.activity = this@MessageActivity
        apiServices = ApiUtils.apiService
        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        val listId = intent.getStringExtra(Constant.MESSAGE_LLIST_ID)
        val vercall: Call<Condole> = apiServices.getConID(listId, prefs.myaccesstoken)
        vercall.enqueue(object : Callback<Condole> {
            override fun onResponse(call: Call<Condole>, response: Response<Condole>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Condole response SUCCESS -> $result")
                    setAdapter(result.condoleMessage!!)
                }
                else {
                    Log.d(TAG,"Condole response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<Condole>, t: Throwable) {
                Log.d(TAG, "Condole FAIL -> $t")
                Log.d(TAG,"parms ->${prefs.myListId}")
            }
        })
    }

    private fun otherAPI() {
        val listId = intent.getStringExtra(Constant.MESSAGE_LLIST_ID)
        val vercall: Call<Condole> = apiServices.getConID(listId, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<Condole> {
            override fun onResponse(call: Call<Condole>, response: Response<Condole>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Condole second response SUCCESS -> $result")
                    setAdapter(result.condoleMessage!!)
                }
                else {
                    Log.d(TAG,"Condole second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<Condole>, t: Throwable) {
                Log.d(TAG, "Condole second FAIL -> $t")
            }
        })
    }

    private fun setAdapter(list: List<CondoleList>) {
        val adapter = MessageRecyclerAdapter(list,this)
        val rv = findViewById<View>(R.id.rc_message) as RecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
    }


    fun onBackClick(v: View) {
        moveList()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onFabMainClick(v: View) {
        putID()
    }

    private fun putID() {
        val id = intent.getStringExtra(Constant.MESSAGE_LLIST_ID)
        val listid = id
        val intent = Intent(this, MessageUploadActivity::class.java)
        intent.putExtra(Constant.MESSAGE_DETAIL_LLIST_ID,listid.toString())
        startActivity(intent)

    }


    override fun onBackPressed() {
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        messageRead?.notifyDataSetChanged()
    }
}