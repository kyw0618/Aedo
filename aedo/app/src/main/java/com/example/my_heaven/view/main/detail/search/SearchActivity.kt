package com.example.my_heaven.view.main.detail.search

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
import com.example.my_heaven.adapter.NoticeAdapter
import com.example.my_heaven.adapter.SearchAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySearchBinding
import com.example.my_heaven.model.notice.NoticeModel
import com.example.my_heaven.model.restapi.base.CreateName
import com.example.my_heaven.model.restapi.base.CreateSearch
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySearchBinding
    private lateinit var apiServices: APIService
    private var searchAdapter : SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        mBinding.activity = this@SearchActivity
        apiServices = ApiUtils.apiService
        inStatusBar()
        initSearchAPI()
    }

    private fun initSearchAPI() {
        val vercall: Call<CreateName> = apiServices.getCreateName(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<CreateName> {
            override fun onResponse(call: Call<CreateName>, response: Response<CreateName>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"NoticeModel response SUCCESS -> $result")
                    setAdapter(result.search!!)
                }
                else {
                    Log.d(LLog.TAG,"NoticeModel response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<CreateName>, t: Throwable) {
                Log.d(LLog.TAG, "NoticeModel Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        val vercall: Call<CreateName> = apiServices.getCreateName(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<CreateName> {
            override fun onResponse(call: Call<CreateName>, response: Response<CreateName>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"NoticeModel response SUCCESS -> $result")
                    setAdapter(result.search!!)
                }
                else {
                    Log.d(LLog.TAG,"NoticeModel response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<CreateName>, t: Throwable) {
                Log.d(LLog.TAG, "NoticeModel Fail -> $t")
            }
        })
    }

    private fun setAdapter(search: List<CreateSearch>) {
        val adapter = SearchAdapter(search,this)
        val rv = findViewById<View>(R.id.search_recyclerView) as RecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    fun onBackClick(v: View){
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        searchAdapter?.notifyDataSetChanged()
    }
}