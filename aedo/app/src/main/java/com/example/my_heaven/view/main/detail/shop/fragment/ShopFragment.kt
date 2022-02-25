package com.example.my_heaven.view.main.detail.shop.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import androidx.recyclerview.widget.GridLayoutManager
import com.example.my_heaven.adapter.Sample
import com.example.my_heaven.adapter.ShopGridLayoutAdapter
import com.example.my_heaven.databinding.FragmentShopBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var mBinding: FragmentShopBinding
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var gridLayoutAdapter: ShopGridLayoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGridLayout()
    }

    private fun setupGridLayout() {
        val gridLayout = mBinding.rvFragmentShop
        gridLayoutAdapter = ShopGridLayoutAdapter()
        gridLayout.adapter = gridLayoutAdapter

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayout.layoutManager = gridLayoutManager

        sampleSubmitList()
    }

    private fun sampleSubmitList() {
        gridLayoutAdapter.submitList(listOf(
            Sample("", "", "", ""),
            Sample("", "", "", ""),
            Sample("", "", "", ""),
            Sample("", "", "", ""),
            Sample("", "", "", ""),
            Sample("", "", "", ""),
            Sample("", "", "", ""),
            Sample("", "", "", "")
        ))
    }

}