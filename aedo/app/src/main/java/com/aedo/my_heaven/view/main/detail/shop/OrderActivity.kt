package com.aedo.my_heaven.view.main.detail.shop

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityOrderBinding
import com.aedo.my_heaven.util.base.BaseActivity

class OrderActivity : BaseActivity() {
    private lateinit var mBinding: ActivityOrderBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_order)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onBackClick(v: View) {
        moveSide()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ShopActivity::class.java))
        finish()
    }
}