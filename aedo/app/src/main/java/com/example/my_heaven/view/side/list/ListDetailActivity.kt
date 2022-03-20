package com.example.my_heaven.view.side.list

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.example.my_heaven.model.restapi.base.Coordinates
import com.example.my_heaven.model.restapi.base.Policy
import com.example.my_heaven.util.`object`.Constant.BURIED
import com.example.my_heaven.util.`object`.Constant.COFFIN_DATE
import com.example.my_heaven.util.`object`.Constant.DECEASED_NAME
import com.example.my_heaven.util.`object`.Constant.DOFP_DATE
import com.example.my_heaven.util.`object`.Constant.EOD_DATE
import com.example.my_heaven.util.`object`.Constant.GPS_ENABLE_REQUEST_CODE
import com.example.my_heaven.util.`object`.Constant.LLIST_ID
import com.example.my_heaven.util.`object`.Constant.MESSAGE_LLIST_ID
import com.example.my_heaven.util.`object`.Constant.PERMISSIONS
import com.example.my_heaven.util.`object`.Constant.PERMISSION_REQUEST_CODE
import com.example.my_heaven.util.`object`.Constant.PLACE_NAME
import com.example.my_heaven.util.`object`.Constant.RESIDENT_NAME
import com.example.my_heaven.util.`object`.Constant.TAG
import com.example.my_heaven.util.alert.LoadingDialog
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.view.side.list.detail.MessageActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.two_button_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListDetailActivity : BaseActivity(),OnMapReadyCallback {
    private lateinit var mBinding: ActivityListdetailBinding
    private lateinit var apiServices: APIService
    private lateinit var datas : Obituaray
    private var readapter: RecyclerAdapter?=null
    private var locationSource: FusedLocationSource? = null

    private var mMap: NaverMap?=null


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

    override fun onResume() {
        super.onResume()
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
    }

    override fun onPause() {
        super.onPause()
        removeGps()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.setIsMainNoticeViewed(false)
    }

    private fun removeGps() {
        setMapTrackingMode(LocationTrackingMode.None)
        locationSource = null
        mMap?.locationSource = null
        mMap?.onMapClickListener = null
    }

    private fun setMapTrackingMode(trackingMode: LocationTrackingMode?) {
        mMap?.locationTrackingMode = trackingMode!!
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER))
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하시겠습니까?")
        builder.setPositiveButton("설정") { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun checkRunTimePermission() {
        // 위치 퍼미션 체크
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    PERMISSIONS[0]
                )) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
            }
            else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                //result.getContents 를 이용 데이터 재가공
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grandResults: IntArray) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults)
        if (permsRequestCode == PERMISSION_REQUEST_CODE && grandResults.size == PERMISSIONS.size) {
            var check_result = true
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1])) {
                    Toast.makeText(this, "권한이 거부되었습니다. 앱을 다시 실행하여 위치 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {
                    Toast.makeText(this, "권한이 거부되었습니다. 설정(앱 정보)에서 위치 권한을 허용해야 합니다. ", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }


    private fun initView() {
        val place_name = intent.getStringExtra(PLACE_NAME)

        val place_first = realm.where(Coordinates::class.java).equalTo("id","PLACE_FIRST").findFirst()
        val place_second  = realm.where(Coordinates::class.java).equalTo("id","PLACE_Second").findFirst()
        val place_third  = realm.where(Coordinates::class.java).equalTo("id","PLACE_Third").findFirst()
        val place_four  = realm.where(Coordinates::class.java).equalTo("id","PLACE_Four").findFirst()

        when {

            place_name.equals(place_first?.name) -> {
                mBinding.tvMapDetail.text = place_first?.address.toString()
                val startPosition = CameraPosition(LatLng(place_first?.xvalue!!.toDouble(),place_first.yvalue!!.toDouble()), 17.2, 0.0, 0.0)
                val options = NaverMapOptions()
                    .camera(startPosition)
                    .mapType(NaverMap.MapType.Basic)
                    .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
                    .compassEnabled(true)
                    .scaleBarEnabled(true)

                val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
                    ?: MapFragment.newInstance(options).also {
                        supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
                    }
                mapFragment.getMapAsync(this)

            }

            place_name.equals(place_second?.name) -> {
                mBinding.tvMapDetail.text = place_second?.address.toString()
                val startPosition = CameraPosition(LatLng(place_second?.xvalue!!.toDouble(),place_second.yvalue!!.toDouble()), 17.2, 0.0, 0.0)
                val options = NaverMapOptions()
                    .camera(startPosition)
                    .mapType(NaverMap.MapType.Basic)
                    .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
                    .compassEnabled(true)
                    .scaleBarEnabled(true)

                val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
                    ?: MapFragment.newInstance(options).also {
                        supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
                    }
                mapFragment.getMapAsync(this)
            }

            place_name.equals(place_third?.name) -> {
                mBinding.tvMapDetail.text = place_third?.address.toString()
                val startPosition = CameraPosition(LatLng(place_third?.xvalue!!.toDouble(),place_third.yvalue!!.toDouble()), 17.2, 0.0, 0.0)
                val options = NaverMapOptions()
                    .camera(startPosition)
                    .mapType(NaverMap.MapType.Basic)
                    .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
                    .compassEnabled(true)
                    .scaleBarEnabled(true)

                val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
                    ?: MapFragment.newInstance(options).also {
                        supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
                    }
                mapFragment.getMapAsync(this)

            }

            place_name.equals(place_four?.name) -> {
                mBinding.tvMapDetail.text = place_four?.address.toString()
                val startPosition = CameraPosition(LatLng(place_four?.xvalue!!.toDouble(),place_four.yvalue!!.toDouble()), 17.2, 0.0, 0.0)
                val options = NaverMapOptions()
                    .camera(startPosition)
                    .mapType(NaverMap.MapType.Basic)
                    .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
                    .compassEnabled(true)
                    .scaleBarEnabled(true)

                val mapFragment = supportFragmentManager.findFragmentById(R.id.naver_map) as MapFragment?
                    ?: MapFragment.newInstance(options).also {
                        supportFragmentManager.beginTransaction().add(R.id.naver_map, it).commit()
                    }
                mapFragment.getMapAsync(this)
            }
            else -> {
                serverDialog()
                return
            }
        }
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
        val place_first = realm.where(Coordinates::class.java).equalTo("id","PLACE_FIRST").findFirst()
        val place_second  = realm.where(Coordinates::class.java).equalTo("id","PLACE_Second").findFirst()
        val place_third  = realm.where(Coordinates::class.java).equalTo("id","PLACE_Third").findFirst()
        val place_four  = realm.where(Coordinates::class.java).equalTo("id","PLACE_Four").findFirst()

        Marker().apply {
            position = LatLng(place_first?.xvalue!!.toDouble(),place_first.yvalue!!.toDouble())
            map = naverMap
        }

        Marker().apply {
            position = LatLng(place_second?.xvalue!!.toDouble(),place_second.yvalue!!.toDouble())
            map = naverMap
        }

        Marker().apply {
            position = LatLng(place_third?.xvalue!!.toDouble(),place_third.yvalue!!.toDouble())
            map = naverMap
        }

        Marker().apply {
            position = LatLng(place_four?.xvalue!!.toDouble(),place_four.yvalue!!.toDouble())
            map = naverMap
        }
    }

    fun onBackClick(v: View) {
        moveMain()
    }

    fun onDataSendClick(v: View) {
        putID()
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
        val id = intent.getStringExtra(LLIST_ID)
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
        val id = intent.getStringExtra(LLIST_ID)
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

    private fun putID() {
        val id = intent.getStringExtra(LLIST_ID)
        val listid = id
        val intent = Intent(this, MessageActivity::class.java)
        intent.putExtra(MESSAGE_LLIST_ID,listid.toString())
        startActivity(intent)
    }

}