package com.aedo.my_heaven.view.main.detail.shop.fragment.order

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityNineOrderBinding
import com.aedo.my_heaven.model.shop.*
import com.aedo.my_heaven.util.`object`.Constant
import com.aedo.my_heaven.util.`object`.Constant.SHOP_NINE
import com.aedo.my_heaven.util.`object`.Constant.SHOP_NINE_PAY
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication
import com.aedo.my_heaven.util.log.LLog
import com.aedo.my_heaven.view.main.detail.shop.ShopActivity
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

class NineOrderActivity : BaseActivity() {
    private lateinit var mBinding: ActivityNineOrderBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_nine_order)
        Iamport.init(this)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvMakeData.text = onlyDate.toString()
        inStatusBar()
        setupSpinnerHandler()
        makeTop()
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val name = intent.getStringExtra(SHOP_NINE).toString()
        val pay = intent.getStringExtra(SHOP_NINE_PAY)

        mBinding.tvFlowerNameDetail.text = name
        mBinding.tvFlowerPayDetail.text = "${pay}???"
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
                mBinding.makeTxPhone.error = "?????????"
            }
            receiver_name.isEmpty() -> {
                mBinding.orderSetPerson.error = "?????????"
            }
            receiver_phone.isEmpty() -> {
                mBinding.orderSetPhone.error = "?????????"
            }
            send_name.isEmpty() -> {
                mBinding.orderSendPerson.error = "?????????"
            }
            send_phone.isEmpty() -> {
                mBinding.orderSendPhone.error = "????????????"
            }
            flower_name.isEmpty() -> {
                mBinding.orderSeondFlower.error = "?????????"
            }
            else -> {
                dialog?.show()
                orderAPI(v)
            }
        }
    }

    private fun orderAPI(v: View) {
        val place = Place(mBinding.spinnerInfoTextTt.text.toString(),mBinding.makeTxPhone.text.toString())
        val item =  mBinding.tvFlowerNameDetail.text.toString()
        val price = mBinding.tvFlowerPayDetail.text.toString()
        val receiver = Receiver(mBinding.orderSetPerson.text.toString(), mBinding.orderSetPhone.text.toString())
        val sender = Sender(mBinding.orderSendPerson.text.toString(), mBinding.orderSendPhone.text.toString())
        val word = Word(mBinding.orderSeondFlower.text.toString(), mBinding.orderTvSendPickText.text.toString(),mBinding.orderSendTvStragith.text.toString())
        val createds = mBinding.tvMakeData.text.toString()
        val data = Orders(place, item, price,receiver, sender, word, created = createds)

        LLog.e("?????? API")
        apiServices.getOrder(MyApplication.prefs.myaccesstoken,data).enqueue(object :
            Callback<ShopModel> {
            override fun onResponse(call: Call<ShopModel>, response: Response<ShopModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"ShopModel  API SUCCESS -> $result")
                    getImport(v)
                }
                else {
                    Log.d(LLog.TAG,"ShopModel  API ERROR -> ${response.errorBody()}")
                    otherAPI(v)
                }
            }

            override fun onFailure(call: Call<ShopModel>, t: Throwable) {
                Log.d(LLog.TAG,"ShopModel  Fail -> $t")
            }
        })
    }

    private fun otherAPI(v: View) {
        val place = Place(mBinding.spinnerInfoTextTt.text.toString(),mBinding.makeTxPhone.text.toString())
        val item =  mBinding.tvFlowerNameDetail.text.toString()
        val price = mBinding.tvFlowerPayDetail.text.toString()
        val receiver = Receiver(mBinding.orderSetPerson.text.toString(), mBinding.orderSetPhone.text.toString())
        val sender = Sender(mBinding.orderSendPerson.text.toString(), mBinding.orderSendPhone.text.toString())
        val word = Word(mBinding.orderSeondFlower.text.toString(), mBinding.orderTvSendPickText.text.toString(),mBinding.orderSendTvStragith.text.toString())
        val createds = mBinding.tvMakeData.text.toString()
        val data = Orders(place, item, price,receiver, sender, word, created = createds)

        LLog.e("??????_????????? API")
        apiServices.getOrder(MyApplication.prefs.newaccesstoken,data).enqueue(object :
            Callback<ShopModel> {
            override fun onResponse(call: Call<ShopModel>, response: Response<ShopModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"ShopModel Second API SUCCESS -> $result")
                    getImport(v)
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

    private fun getImport(v: View) {
        val request = IamPortRequest(
            pg = PG.nice.makePgRawName(""),         // PG???
            pay_method = PayMethod.card.name,                    // ????????????
            name = mBinding.tvFlowerNameDetail.text.toString(),                      // ?????????
            merchant_uid = "sample_aos_${Date().time}",     // ????????????
            amount =  "25000",                                // ????????????
            buyer_name = mBinding.orderSendPerson.text.toString()                             // ????????? ??????
        )

        Snackbar.make(v, "????????? ?????? ???????????????????", Snackbar.LENGTH_LONG)
            .setAction("??????") {
                val userCode = "imp00383227"
                Log.d("??????", "???????????????????")
                Iamport.close()
                // ???????????? SDK ??? ?????? ????????????
                Iamport.payment(userCode, iamPortRequest = request, paymentResultCallback = {
                    moveShop()
                })
            }.show()
    }

    fun onShopTermClick(v: View) {
        moveShopTerm()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ShopActivity::class.java))
        finish()
    }
}