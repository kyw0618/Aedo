package com.aedo.my_heaven.view.main.detail.shop.fragment

import android.annotation.SuppressLint
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

    @SuppressLint("ShowToast")
    private fun setClick() {

        mBinding.btnActivityShopPurchase.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            intent.putExtra(SHOP_FIRST,mBinding.tvActivityShopTitle.text.toString())
            intent.putExtra(SHOP_FIRST_PAY, mBinding.tvActivityShopSalesPrice.text.toString())
            startActivity(intent)
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