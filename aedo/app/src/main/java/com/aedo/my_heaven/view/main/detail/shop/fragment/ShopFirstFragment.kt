package com.aedo.my_heaven.view.main.detail.shop.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.databinding.FragmentShopFirstBinding
import com.aedo.my_heaven.view.main.detail.shop.OrderActivity

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
            val intent = Intent(activity, OrderActivity::class.java)
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