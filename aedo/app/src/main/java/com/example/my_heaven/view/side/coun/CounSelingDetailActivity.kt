package com.example.my_heaven.view.side.coun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.adapter.CounSelingAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityCounSelingDetailBinding
import com.example.my_heaven.databinding.ActivityCounselingBinding
import com.example.my_heaven.util.`object`.Constant
import com.example.my_heaven.util.`object`.Constant.COUN_CONTENT
import com.example.my_heaven.util.`object`.Constant.COUN_CREATED
import com.example.my_heaven.util.`object`.Constant.COUN_NAME
import com.example.my_heaven.util.`object`.Constant.COUN_TITLE
import com.example.my_heaven.util.`object`.Constant.NOTICE_TITLE
import com.example.my_heaven.util.base.BaseActivity

class CounSelingDetailActivity : BaseActivity() {
    private lateinit var mBinding: ActivityCounSelingDetailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_coun_seling_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        inRecycler()
    }

    private fun inRecycler() {
        val name = intent.getStringExtra(COUN_NAME)
        val title = intent.getStringExtra(COUN_TITLE)
        val content = intent.getStringExtra(COUN_CONTENT)
        val created = intent.getStringExtra(COUN_CREATED)

        mBinding.tvCounselingTitle.text = title.toString()
        mBinding.tvCounselingTimestamp.text = created.toString()
        mBinding.tvQName.text = name.toString()
        mBinding.tvCounselingDetail.text = content.toString()
    }

    fun onBackClick(v: View) {
        moveCounseling()
    }

    fun onCounClick(v: View) {
        moveCounseling()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, CounselingActivity::class.java))
        finish()
    }
}