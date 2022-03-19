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
import com.example.my_heaven.databinding.ActivityUploadCounselingBinding
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.view.main.MainActivity
import com.example.my_heaven.viewmodel.AgreeViewModel
import java.time.LocalDate

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
        initView()
    }

    private fun initView() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvTime.text = onlyDate.toString()
    }

    fun onBackClick(v: View) {
        moveCounseling()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onDataSendClick(v: View) {
        val name = mBinding.etName.text.toString()
        val phone = mBinding.etEmail.text.toString()
        val title = mBinding.etConTitle.text.toString()
        val detail = mBinding.etConDetail.text.toString()
        val time = mBinding.tvTime
        when {
            name.isEmpty() -> {
                mBinding.etName.error = "미입력"
            }
            phone.isEmpty() -> {
                mBinding.etEmail.error = "미입력"
            }
            title.isEmpty() -> {
                mBinding.etConTitle.error = "미입력"
            }
            detail.isEmpty() -> {
                mBinding.etConDetail.error = "미입력"
            }
            else -> {
                dialog?.show()
                counSelingAPI()
            }
        }
    }

    private fun counSelingAPI() {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        startActivity(Intent(this, CounselingActivity::class.java))
        finish()
    }
}