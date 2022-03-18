package com.example.my_heaven.view.side.list

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_heaven.R
import com.example.my_heaven.adapter.RecyclerAdapter
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityListBinding
import com.example.my_heaven.model.list.Obituaray
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.util.`object`.Constant.BURIED
import com.example.my_heaven.util.`object`.Constant.COFFIN_DATE
import com.example.my_heaven.util.`object`.Constant.DECEASED_NAME
import com.example.my_heaven.util.`object`.Constant.DOFP_DATE
import com.example.my_heaven.util.`object`.Constant.EOD_DATE
import com.example.my_heaven.util.`object`.Constant.PLACE_NAME
import com.example.my_heaven.util.`object`.Constant.RESIDENT_NAME
import com.example.my_heaven.util.alert.LoadingDialog
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.util.log.LLog.TAG
import com.example.my_heaven.view.main.MainActivity
import kotlinx.android.synthetic.main.list_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : BaseActivity() {
    private lateinit var mBinding: ActivityListBinding
    private lateinit var apiServices: APIService
    private var readapter: RecyclerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        mBinding.activity=this@ListActivity
        apiServices = ApiUtils.apiService
        dialog = LoadingDialog(this)
        inStatusBar()
        inRecycler()
    }


    private fun inRecycler() {
        val vercall: Call<RecyclerList> = apiServices.getCreateGet(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<RecyclerList> {
            override fun onResponse(call: Call<RecyclerList>, response: Response<RecyclerList>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"List response SUCCESS -> $result")
                    setAdapter(result.obituary)
                }
                else {
                    Log.d(TAG,"List response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
                Log.d(TAG, "List error -> $t")
            }
        })
    }

    private fun otherAPI() {
        val vercall: Call<RecyclerList> = apiServices.getCreateGet(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<RecyclerList> {
            override fun onResponse(call: Call<RecyclerList>, response: Response<RecyclerList>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"List response SUCCESS -> $result")
                    setAdapter(result.obituary)
                }
                else {
                    Log.d(TAG,"List response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
                Log.d(TAG, "List error -> $t")
            }
        })
    }

    private fun setAdapter(obituary: List<Obituaray>?) {
        val mAdapter = obituary?.let {
            RecyclerAdapter(it,this)
        }
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        return
    }

    fun onBackClick(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onMainClick(v: View) {
        moveMain()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        readapter?.notifyDataSetChanged()
    }


}