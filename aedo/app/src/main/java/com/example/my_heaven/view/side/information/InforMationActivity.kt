package com.example.my_heaven.view.side.information

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityInforMationBinding
import com.example.my_heaven.util.base.BaseActivity

class InforMationActivity : BaseActivity() {
    private lateinit var mBinding: ActivityInforMationBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_infor_mation)
        mBinding.activity = this
        apiServices = ApiUtils.apiService

    }

     fun onBackClick(v: View) {
         moveSide()
     }
}