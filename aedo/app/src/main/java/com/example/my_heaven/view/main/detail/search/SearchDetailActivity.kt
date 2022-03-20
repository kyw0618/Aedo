package com.example.my_heaven.view.main.detail.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySearchBinding
import com.example.my_heaven.databinding.ActivitySearchDetailBinding
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.view.main.MainActivity

class SearchDetailActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySearchDetailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
    }
    fun onBackClick(v: View){
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}