package com.example.my_heaven.view.side.list.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.adapter.MessageRecyclerAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityMessageBinding
import com.example.my_heaven.databinding.ActivityWaringBinding
import com.example.my_heaven.model.list.Obituaray
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.model.restapi.base.ConID
import com.example.my_heaven.model.restapi.base.Condole
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.view.main.MainActivity
import com.example.my_heaven.view.side.list.ListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MessageActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMessageBinding
    private lateinit var apiServices: APIService
    lateinit var messageRecyclerAdapter: MessageRecyclerAdapter
    val datas = mutableListOf<Condole>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waring)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        initRecyclerView()
        inRecycler()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        messageRecyclerAdapter = MessageRecyclerAdapter(this)
        mBinding.recyclerView.adapter = messageRecyclerAdapter

        datas.apply {
            add(Condole(title = "삼가 고인의 명복을 빕니다",content = null, created =null, obld = "애도 임직원 일동"))
            messageRecyclerAdapter.datas = datas
            messageRecyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun inRecycler() {
        val vercall: Call<ConID> = apiServices.getConID()
        vercall.enqueue(object : Callback<ConID> {
            override fun onResponse(call: Call<ConID>, response: Response<ConID>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"List response SUCCESS -> $result")

                }
                else {
                    Log.d(LLog.TAG,"List response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ConID>, t: Throwable) {
                Log.d(LLog.TAG, "List error -> $t")
            }
        })
    }

    fun onBackClick(v: View) {
        moveList()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onFabMainClick(v: View) {
        moveMessageUpload()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }
}