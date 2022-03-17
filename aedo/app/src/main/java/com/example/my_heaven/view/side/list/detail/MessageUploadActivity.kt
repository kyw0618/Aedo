package com.example.my_heaven.view.side.list.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityMessageUploadBinding
import com.example.my_heaven.model.list.*
import com.example.my_heaven.model.restapi.base.*
import com.example.my_heaven.model.restapi.base.Content
import com.example.my_heaven.model.restapi.base.Created
import com.example.my_heaven.model.restapi.base.Obld
import com.example.my_heaven.model.restapi.base.Title
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MessageUploadActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMessageUploadBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waring)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_upload)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        initTime()
    }

    private fun initTime() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvMessageTimestamp.text = onlyDate.toString()
    }

    private fun condleAPI() {
        val title = Title(mBinding.messageTitle.text.toString())
        val content = Content(null)
        val created = Created(mBinding.tvMessageTimestamp.text.toString())
        val obld = Obld(mBinding.messageDetailName.text.toString())
        val data = CreateMessage(title, content, created, obld)
        apiServices.getCondole(prefs.myaccesstoken,data).enqueue(object : Callback<CreateMessage> {
            override fun onResponse(call: Call<CreateMessage>, response: Response<CreateMessage>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"condleAPI SUCCESS -> $result")
                    moveMessage()
                }
                else {
                    Log.d(TAG,"condleAPI ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<CreateMessage>, t: Throwable) {
                Log.d(TAG,"condleAPI FAIL -> $t")
            }
        })

    }

    private fun otherAPI() {
        val title = Title(mBinding.messageTitle.text.toString())
        val content = Content(null)
        val created = Created(mBinding.tvMessageTimestamp.text.toString())
        val obld = Obld(mBinding.messageDetailName.text.toString())
        val data = CreateMessage(title, content, created, obld)
        apiServices.getCondole(prefs.newaccesstoken,data).enqueue(object : Callback<CreateMessage> {
            override fun onResponse(call: Call<CreateMessage>, response: Response<CreateMessage>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"condleAPI SUCCESS -> $result")
                    moveMessage()
                }
                else {
                    Log.d(TAG,"condleAPI ERROR -> ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<CreateMessage>, t: Throwable) {
                Log.d(TAG,"condleAPI FAIL -> $t")
            }
        })
    }

    fun onBackClick(v: View) {
        moveMessage()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onOkClick(v: View) {
        val title = mBinding.messageTitle.text.toString()
        val content = null
        val created = mBinding.tvMessageTimestamp.text.toString()
        val obld = mBinding.messageDetailName.text.toString()
        when {
            title.isEmpty() -> {
                mBinding.messageTitle.error = "미입력"
                Toast.makeText(this,"메세지를 입력해 주세요", Toast.LENGTH_SHORT).show().toString()
            }
            obld.isEmpty() -> {
                mBinding.messageDetailName.error = "미입력"
                Toast.makeText(this,"성함을 입력해 주세요", Toast.LENGTH_SHORT).show().toString()
            }
            else -> {
                condleAPI()
            }

        }

    }
}