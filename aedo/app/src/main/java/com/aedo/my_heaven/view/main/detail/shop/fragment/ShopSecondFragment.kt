package com.aedo.my_heaven.view.main.detail.shop.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.databinding.FragmentShopSecondBinding

class ShopSecondFragment : Fragment() {

    private lateinit var mBinding: FragmentShopSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop_second,container,false)
        return mBinding.root
    }

}