package com.example.my_heaven.view.side

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySideMenuBinding
import com.example.my_heaven.databinding.ActivityThanksBinding
import com.example.my_heaven.util.base.BaseActivity

class ThanksActivity : BaseActivity() {
    private lateinit var mBinding: ActivityThanksBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_thanks)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onBackClick(v: View) {
        moveSide()
    }
}