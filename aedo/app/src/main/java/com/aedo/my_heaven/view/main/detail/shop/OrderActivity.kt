package com.aedo.my_heaven.view.main.detail.shop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityOrderBinding
import com.aedo.my_heaven.util.base.BaseActivity
import java.time.LocalDate

class OrderActivity : BaseActivity() {
    private lateinit var mBinding: ActivityOrderBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_order)
        mBinding.activity = this
        apiServices = ApiUtils.apiService

        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvMakeData.text = onlyDate.toString()

        inStatusBar()
        setupSpinnerHandler()
        makeTop()
    }

    private fun setupSpinnerHandler() {
        mBinding.makeTxSpinnerInfor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBinding.spinnerInfoTextTt.text = mBinding.makeTxSpinnerInfor.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        mBinding.orderSendPickText.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBinding.orderTvSendPickText.text = mBinding.orderSendPickText.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun makeTop() {
        val place = resources.getStringArray(R.array.spinner_place)
        val placeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, place)
        mBinding.makeTxSpinnerInfor.adapter = placeAdapter

        val flower = resources.getStringArray(R.array.spinner_text)
        val flowerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, flower)
        mBinding.orderSendPickText.adapter = flowerAdapter

        mBinding.tvStright.setOnClickListener {
            if(mBinding.orderSendTvStragith.visibility == View.GONE) {
                mBinding.orderSendTvStragith.visibility = View.VISIBLE
            }
            else {
                mBinding.orderSendTvStragith.visibility = View.GONE
            }
        }
    }

    fun onBackClick(v: View) {
        moveShop()
    }

    fun onOkClick(v: View) {

    }

    fun onShopTermClick(v: View) {
        moveShopTerm()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ShopActivity::class.java))
        finish()
    }
}