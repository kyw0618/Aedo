package com.example.my_heaven.view.side.list

import android.os.Bundle
import android.util.Log
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
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListDetailActivity : BaseActivity() {
    private lateinit var mBinding: ActivityListdetailBinding
    private lateinit var apiServices: APIService
    private var item : Obituaray?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listdetail)
        mBinding.activity=this@ListDetailActivity
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
                    Log.d(LLog.TAG,"List response SUCCESS -> $result")
                    putIntent()
                }
                else {
                    Log.d(LLog.TAG,"List response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
                Log.d(LLog.TAG, "List error -> $t")
            }
        })
    }

    private fun putIntent() {

        if(item!=null) {
            mBinding.textInfo.text = item!!.place?.place_name.toString()
            mBinding.txPersonName.text = item!!.deceased?.name.toString()
            mBinding.txData.text = item!!.eod?.date.toString()
            mBinding.txCenterName.text = item!!.resident?.name.toString()
            mBinding.txInfo.text = item!!.place?.place_name.toString()
            mBinding.txDetailCoffin.text = item!!.eod?.date.toString()
            mBinding.txDetailDofp.text = item!!.dofp?.date.toString()
            mBinding.txBuried.text = item!!.buried.toString()
        }
        else {
            Toast.makeText(this,"다시 시도해 주세요",Toast.LENGTH_SHORT).show()
        }

    }

    fun onBackClick(v: View) {
        moveMain()
    }
}