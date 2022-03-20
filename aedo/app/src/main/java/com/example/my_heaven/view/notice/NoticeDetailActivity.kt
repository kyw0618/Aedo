package com.example.my_heaven.view.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityNoticeBinding
import com.example.my_heaven.databinding.ActivityNoticeDetailBinding
import com.example.my_heaven.util.`object`.Constant
import com.example.my_heaven.util.`object`.Constant.NOTICE_CONTENT
import com.example.my_heaven.util.`object`.Constant.NOTICE_CREATED
import com.example.my_heaven.util.`object`.Constant.NOTICE_TITLE
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.view.main.MainActivity

class NoticeDetailActivity : BaseActivity() {
    private lateinit var mBinding: ActivityNoticeDetailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notice_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        val title = intent.getStringExtra(NOTICE_TITLE)
        val content = intent.getStringExtra(NOTICE_CONTENT)
        val created = intent.getStringExtra(NOTICE_CREATED)

        mBinding.tvNoticeDetailTitle.text = title.toString()
        mBinding.tvNoticeDetail.text = content.toString()
        mBinding.tvNoticeTimestamp.text = created.toString()
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, NoticeActivity::class.java))
        finish()
    }
}