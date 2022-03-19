package com.example.my_heaven.view.side.coun

import android.content.Intent
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
import com.example.my_heaven.view.main.SideMenuActivity

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

    override fun onBackPressed() {
        startActivity(Intent(this, SideMenuActivity::class.java))
        finish()
    }
}