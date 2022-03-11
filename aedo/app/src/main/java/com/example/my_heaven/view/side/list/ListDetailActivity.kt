package com.example.my_heaven.view.side.list

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityListdetailBinding
import com.example.my_heaven.model.list.Obituaray
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.util.alert.LoadingDialog
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListDetailActivity : BaseActivity(),OnMapReadyCallback {
    private lateinit var mBinding: ActivityListdetailBinding
    private lateinit var apiServices: APIService
    private lateinit var datas : Obituaray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listdetail)
        mBinding.activity=this@ListDetailActivity
        apiServices = ApiUtils.apiService
        dialog = LoadingDialog(this)
        inStatusBar()
        inRecycler()
        initView()
    }

    private fun initView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun inRecycler() {
        datas = intent.getSerializableExtra("data") as Obituaray

        mBinding.textInfo.text = datas.place?.place_name.toString()
        mBinding.txPersonName.text = datas.deceased?.name.toString()
        mBinding.txData.text = datas.eod?.date.toString()
        mBinding.txCenterName.text = datas.resident?.name.toString()
        mBinding.txInfo.text = datas.place?.place_name.toString()
        mBinding.txDetailCoffin.text = datas.coffin?.date.toString()
        mBinding.txDetailDofp.text = datas.dofp?.date.toString()
        mBinding.txBuried.text = datas.buried.toString()

    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun onMapReady(naverMap: NaverMap) {
    }

    fun onBackClick(v: View) {
        moveMain()
    }

    fun onDataSendClick(v: View) {
        moveMessage()
    }

    fun onWaringClick(v: View) {
        moveWaring()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    fun onSideClick(v : View) {
        listdelete()
    }
}