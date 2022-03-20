package com.example.my_heaven.view.side.setting

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySettingBinding
import com.example.my_heaven.model.restapi.login.GetUser
import com.example.my_heaven.model.restapi.login.LogOut
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySettingBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=DataBindingUtil.setContentView(this, R.layout.activity_setting)
        mBinding.activity = this@SettingActivity
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onLogOutClick(v: View) {
        logOutAPI()
    }

    private fun logOutAPI() {
        apiServices.getLogOut(MyApplication.prefs.myaccesstoken).enqueue(object :
            Callback<LogOut> {
            override fun onResponse(call: Call<LogOut>, response: Response<LogOut>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser API SUCCESS -> $result")
                    moveLogins()
                }
                else {
                    Log.d(LLog.TAG,"GetUser API ERROR -> ${response.errorBody()}")

                }
            }
            override fun onFailure(call: Call<LogOut>, t: Throwable) {
                Log.d(LLog.TAG,"GetUser ERROR -> $t")

            }
        })
    }

    fun onOutClick(v: View) {
        logOutAPI()
    }

    fun onBackClick(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}