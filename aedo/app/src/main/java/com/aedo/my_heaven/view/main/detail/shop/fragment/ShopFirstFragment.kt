package com.aedo.my_heaven.view.main.detail.shop.fragment

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.databinding.FragmentShopFirstBinding
import com.aedo.my_heaven.util.`object`.Constant.SHOP_FIRST
import com.aedo.my_heaven.util.`object`.Constant.SHOP_FIRST_PAY
import com.aedo.my_heaven.view.main.detail.shop.OrderActivity
import com.google.android.material.snackbar.Snackbar
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.PG
import com.iamport.sdk.data.sdk.PayMethod
import com.iamport.sdk.domain.core.Iamport
import java.util.*

class ShopFirstFragment : Fragment() {

    private lateinit var mBinding : FragmentShopFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop_first,container,false)
        setClick()
        return mBinding.root
    }

    private fun setClick() {
        mBinding.btnActivityShopPurchase.setOnClickListener {
//            val intent = Intent(activity, OrderActivity::class.java)
//            intent.putExtra(SHOP_FIRST,mBinding.tvActivityShopTitle.text.toString())
//            intent.putExtra(SHOP_FIRST_PAY, mBinding.tvActivityShopSalesPrice.text.toString())
//            startActivity(intent)
            val request = IamPortRequest(
                pg = PG.nice.makePgRawName(""),         // PG사
                pay_method = PayMethod.card.name,                    // 결제수단
                name = mBinding.tvActivityShopTitle.text.toString(),                      // 주문명
                merchant_uid = "sample_aos_${Date().time}",     // 주문번호
                amount = mBinding.tvActivityShopSalesPrice.text.toString(),                                // 결제금액
                buyer_name = "김개발"
            )

            mBinding.btnActivityShopPurchase.setOnClickListener { view ->
                Snackbar.make(view, "결제를 진행 하시겠습니까?",Snackbar.LENGTH_LONG)
                    .setAction("결제") {
                        val userCode = "imp00383227"
                        Log.d("하이", "결제시작인데?")
                        Iamport.close()
                        // 아임포트 SDK 에 결제 요청하기
                        Iamport.payment(userCode, iamPortRequest = request, paymentResultCallback = {
                        })

                    }.show()
            }
        }
        mBinding.btnSecond.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            startActivity(intent)
        }
        mBinding.btnThird.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            startActivity(intent)
        }
        mBinding.btnFour.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            startActivity(intent)
        }
    }
}