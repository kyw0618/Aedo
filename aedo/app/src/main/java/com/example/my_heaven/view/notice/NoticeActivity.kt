package com.example.my_heaven.view.notice

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_heaven.R
import com.example.my_heaven.adapter.NoticeAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityModifyBinding
import com.example.my_heaven.databinding.ActivityNoticeBinding
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.model.notice.Announcement
import com.example.my_heaven.model.notice.NoticeModel
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : BaseActivity() {
    private lateinit var mBinding: ActivityNoticeBinding
    private lateinit var apiServices: APIService
    private var noticeAdapter : NoticeAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notice)
        mBinding.activity = this@NoticeActivity
        apiServices = ApiUtils.apiService
        mBinding.medicalRecyclerView.adapter = noticeAdapter
        mBinding.medicalRecyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.medicalRecyclerView.setHasFixedSize(true)
        mBinding.medicalRecyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        inStatusBar()
        inNoticeAPI()
    }

    private fun inNoticeAPI() {
        val vercall: Call<NoticeModel> = apiServices.getNoti(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<NoticeModel> {
            override fun onResponse(call: Call<NoticeModel>, response: Response<NoticeModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"NoticeModel response SUCCESS -> $result")
                }
                else {
                    Log.d(LLog.TAG,"NoticeModel response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<NoticeModel>, t: Throwable) {
                Log.d(LLog.TAG, "NoticeModel Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        val vercall: Call<NoticeModel> = apiServices.getNoti(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<NoticeModel> {
            override fun onResponse(call: Call<NoticeModel>, response: Response<NoticeModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"NoticeModel Second response SUCCESS -> $result")
                }
                else {
                    Log.d(LLog.TAG,"NoticeModel Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<NoticeModel>, t: Throwable) {
                Log.d(LLog.TAG, "NoticeModel Second Fail -> $t")
            }
        })
    }



    fun onBackClick(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        noticeAdapter?.notifyDataSetChanged()
    }

}