package com.example.my_heaven.view.side.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.adapter.MessageRecyclerAdapter
import com.example.my_heaven.adapter.RecyclerAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityListdetailBinding
import com.example.my_heaven.model.list.Condole
import com.example.my_heaven.model.list.ListDelete
import com.example.my_heaven.model.list.Obituaray
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.util.`object`.Constant.BURIED
import com.example.my_heaven.util.`object`.Constant.COFFIN_DATE
import com.example.my_heaven.util.`object`.Constant.DECEASED_NAME
import com.example.my_heaven.util.`object`.Constant.DOFP_DATE
import com.example.my_heaven.util.`object`.Constant.EOD_DATE
import com.example.my_heaven.util.`object`.Constant.LIST_ID
import com.example.my_heaven.util.`object`.Constant.PLACE_NAME
import com.example.my_heaven.util.`object`.Constant.RESIDENT_NAME
import com.example.my_heaven.util.alert.LoadingDialog
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kotlinx.android.synthetic.main.two_button_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListDetailActivity : BaseActivity(),OnMapReadyCallback {
    private lateinit var mBinding: ActivityListdetailBinding
    private lateinit var apiServices: APIService
    private lateinit var datas : Obituaray
    private var readapter: RecyclerAdapter?=null

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

    @SuppressLint("SetTextI18n")
    private fun inRecycler() {
        val decase_name = intent.getStringExtra(DECEASED_NAME)
        val place_name = intent.getStringExtra(PLACE_NAME)
        val tx_data = intent.getStringExtra(EOD_DATE)
        val tv_center_name = intent.getStringExtra(RESIDENT_NAME)
        val coffin = intent.getStringExtra(COFFIN_DATE)
        val dofp = intent.getStringExtra(DOFP_DATE)
        val buried = intent.getStringExtra(BURIED)
        mBinding.txPersonName.text = decase_name.toString()
        mBinding.textInfo.text = place_name.toString()
        mBinding.txData.text = "$tx_data 별세"
        mBinding.txCenterName.text = tv_center_name.toString()
        mBinding.txInfo.text = place_name.toString()
        mBinding.txDetailCoffin.text = coffin.toString()
        mBinding.txDetailDofp.text = dofp.toString()
        mBinding.txBuried.text = buried.toString()
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
        val myLayout = layoutInflater.inflate(R.layout.two_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.list_delete)
        val dialog = build.create()
        dialog.show()

        myLayout.finish_btn.text = getString(R.string.btn_delete)
        myLayout.update_btn.text = getString(R.string.btn_modify)

        myLayout.finish_btn.setOnClickListener {

            delete()
            dialog.dismiss()
        }
        myLayout.update_btn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
    }

    private fun delete() {
        val id = intent.getStringExtra(LIST_ID)
        val vercall: Call<ListDelete> = apiServices.getCreateDelete(id, prefs.myaccesstoken)
        vercall.enqueue(object : Callback<ListDelete> {
            override fun onResponse(call: Call<ListDelete>, response: Response<ListDelete>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"ListDelete response SUCCESS -> $result")
                    moveList()
                }
                else {
                    Log.d(LLog.TAG,"ListDelete response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<ListDelete>, t: Throwable) {
                Log.d(LLog.TAG, "ListDelete FAIL -> $t")
            }
        })
    }

    private fun otherAPI() {
        val id = intent.getStringExtra(LIST_ID)
        val vercall: Call<Condole> = apiServices.getConID(id, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<Condole> {
            override fun onResponse(call: Call<Condole>, response: Response<Condole>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"ListDelete second response SUCCESS -> $result")
                    moveList()
                }
                else {
                    Log.d(LLog.TAG,"ListDelete second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<Condole>, t: Throwable) {
                Log.d(LLog.TAG, "ListDelete second FAIL -> $t")
            }
        })
    }
}