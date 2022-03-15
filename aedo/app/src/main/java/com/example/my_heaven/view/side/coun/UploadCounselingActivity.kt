package com.example.my_heaven.view.side.coun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityUploadCounselingBinding
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.viewmodel.AgreeViewModel

class UploadCounselingActivity : BaseActivity() {
    private lateinit var mBinding: ActivityUploadCounselingBinding
    private lateinit var apiServices: APIService
    private val mViewModel : AgreeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_upload_counseling)
        mBinding.activity = this
        mBinding.vm = mViewModel
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    fun onDataSendClick(v: View) {
        moveCounseling()
    }
}