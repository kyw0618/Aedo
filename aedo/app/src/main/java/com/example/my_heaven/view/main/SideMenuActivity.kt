package com.example.my_heaven.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySideMenuBinding
import com.example.my_heaven.model.restapi.base.CreateModel
import com.example.my_heaven.model.restapi.login.GetUser
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SideMenuActivity : BaseActivity() {
    private lateinit var mBinding : ActivitySideMenuBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_side_menu)
        mBinding.activity=this@SideMenuActivity
        apiServices = ApiUtils.apiService
        inStatusBar()
        initAPI()
    }

    private fun initAPI() {
        apiServices.getUser(prefs.myaccesstoken).enqueue(object :
            Callback<GetUser> {
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser API SUCCESS -> $result")
                    mBinding.sideTxName.text = result.user?.name.toString()
                }
                else {
                    Log.d(LLog.TAG,"GetUser API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Log.d(LLog.TAG,"GetUser ERROR -> $t")

            }
        })
    }

    private fun otherAPI() {
        apiServices.getUser(prefs.newaccesstoken).enqueue(object :
            Callback<GetUser> {
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser API SUCCESS -> $result")
                    mBinding.sideTxName.text = result.user?.name.toString()
                }
                else {
                    Log.d(LLog.TAG,"GetUser API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Log.d(LLog.TAG,"GetUser ERROR -> $t")

            }
        })
    }

    fun onBackClick(v: View?) {
        moveMain()
    }

    fun onInfoClick(v:View) {
        moveInfor()
    }

    fun onListClick(v:View) {
        moveList()
    }

    fun onSetClick(v: View) {
        moveSetting()
    }

    fun onCompany(v: View) {
        moveInfor()
    }

    fun onThanksClick(v: View) {

    }

    fun onFAQClick(v: View) {

    }

    fun onKAKAOClick(v: View) {

    }

    fun onCounClick(v: View) {
        moveCounseling()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}