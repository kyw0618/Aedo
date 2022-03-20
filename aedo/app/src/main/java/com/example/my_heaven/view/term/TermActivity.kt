package com.example.my_heaven.view.term

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySettingBinding
import com.example.my_heaven.databinding.ActivityTermBinding
import com.example.my_heaven.model.restapi.base.AutoLogin
import com.example.my_heaven.model.restapi.base.Terms
import com.example.my_heaven.model.restapi.base.TremModel
import com.example.my_heaven.model.restapi.login.GetUser
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.view.main.MainActivity
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermActivity : BaseActivity() {
    private lateinit var mBinding: ActivityTermBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_term)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        initTermAPI()
    }

    private fun initTermAPI() {
        apiServices.getTerms().enqueue(object : Callback<TremModel> {
            override fun onResponse(call: Call<TremModel>, response: Response<TremModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"TremModel API SUCCESS -> $result")
                    terms(result.terms)
                }
                else {
                    Log.d(LLog.TAG,"TremModel API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TremModel>, t: Throwable) {
                Log.d(LLog.TAG,"TremModel ERROR -> $t")

            }
        })
    }

    private fun terms(terms: Terms?) {
        mBinding.tvFirst.text = terms?.title
        mBinding.tvSubTitle.text = terms?.sub_title
        mBinding.tvDetailTermsFirst.text = terms?.first
        mBinding.tvDetailTermsSecond.text = terms?.second
        mBinding.tvDetailTermsThird.text = terms?.third
        mBinding.tvDetailTermsFour.text = terms?.fourth
        mBinding.tvDetailTermsFive.text = terms?.fifth
    }

    fun onBackClick(v: View) {
        moveSide()
    }


    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}