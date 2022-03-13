package com.example.my_heaven.view.main

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySideMenuBinding
import com.example.my_heaven.util.base.BaseActivity

class SideMenuActivity : BaseActivity() {
    private lateinit var mBinding : ActivitySideMenuBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_side_menu)
        mBinding.activity=this@SideMenuActivity
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onBackClick(v: View?) {
        moveMain()
    }

    fun onInfoClick(v:View) {
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
}