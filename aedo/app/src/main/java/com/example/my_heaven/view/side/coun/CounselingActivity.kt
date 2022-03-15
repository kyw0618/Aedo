package com.example.my_heaven.view.side.coun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityCounselingBinding
import com.example.my_heaven.util.base.BaseActivity

class CounselingActivity : BaseActivity() {
    private lateinit var mBinding: ActivityCounselingBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_counseling)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    fun onCounClick(v: View) {
        moveUploadCoun()
    }
}