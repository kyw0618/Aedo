package com.aedo.my_heaven.view.main.detail.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityOrderBinding
import com.aedo.my_heaven.model.shop.*
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.util.log.LLog
import com.google.android.material.snackbar.Snackbar
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.PG
import com.iamport.sdk.data.sdk.PayMethod
import com.iamport.sdk.domain.core.Iamport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*

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
        val place_number =  mBinding.makeTxPhone.text.toString()
        val receiver_name = mBinding.orderSetPerson.text.toString()
        val receiver_phone = mBinding.orderSetPhone.text.toString()
        val send_name = mBinding.orderSendPerson.text.toString()
        val send_phone = mBinding.orderSendPhone.text.toString()
        val flower_name = mBinding.orderSeondFlower.text.toString()

        when {
            place_number.isEmpty() -> {
                mBinding.makeTxPhone.error = "미입력"
            }
            receiver_name.isEmpty() -> {
                mBinding.orderSetPerson.error = "미입력"
            }
            receiver_phone.isEmpty() -> {
                mBinding.orderSetPhone.error = "미입력"
            }
            send_name.isEmpty() -> {
                mBinding.orderSendPerson.error = "미입력"
            }
            send_phone.isEmpty() -> {
                mBinding.orderSendPhone.error = "미입력력"
           }
            flower_name.isEmpty() -> {
                mBinding.orderSeondFlower.error = "미입력"
            }
            else -> {
                dialog?.show()
                orderAPI()
            }
        }
    }

    private fun orderAPI() {
        val place = Place(mBinding.spinnerInfoTextTt.text.toString(),mBinding.makeTxPhone.text.toString())
        val receiver = Receiver()
        val sender = Sender()
        val word = Word()
        val data = Orders(place, receiver, sender, word)

        LLog.e("주문 API")
        apiServices.getOrder(prefs.myaccesstoken,data).enqueue(object :
            Callback<ShopModel> {
            override fun onResponse(call: Call<ShopModel>, response: Response<ShopModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"ShopModel  API SUCCESS -> $result")
                    getImport()
                }
                else {
                    Log.d(LLog.TAG,"ShopModel  API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<ShopModel>, t: Throwable) {
                Log.d(LLog.TAG,"ShopModel  Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        val place = Place(mBinding.spinnerInfoTextTt.text.toString(),mBinding.makeTxPhone.text.toString())
        val receiver = Receiver()
        val sender = Sender()
        val word = Word()
        val data = Orders(place, receiver, sender, word)

        LLog.e("주문_두번째 API")
        apiServices.getOrder(prefs.newaccesstoken,data).enqueue(object :
            Callback<ShopModel> {
            override fun onResponse(call: Call<ShopModel>, response: Response<ShopModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"ShopModel Second API SUCCESS -> $result")
                    getImport()
                }
                else {
                    Log.d(LLog.TAG,"ShopModel Second API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ShopModel>, t: Throwable) {
                Log.d(LLog.TAG,"ShopModel Second Fail -> $t")
            }
        })
    }

    private fun getImport() {

    }

    fun onShopTermClick(v: View) {
        moveShopTerm()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ShopActivity::class.java))
        finish()
    }
}