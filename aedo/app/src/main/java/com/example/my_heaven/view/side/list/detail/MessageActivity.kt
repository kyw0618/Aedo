package com.example.my_heaven.view.side.list.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityMessageBinding
import com.example.my_heaven.databinding.ActivityWaringBinding
import com.example.my_heaven.util.base.BaseActivity

class MessageActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMessageBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waring)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()


    }

    fun onBackClick(v: View) {
        moveList()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onFabMainClick(v: View) {
        moveMessageUpload()
    }
}