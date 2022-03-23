package com.aedo.my_heaven.view.main.detail.make

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityMakeBinding
import com.aedo.my_heaven.model.restapi.base.*
import com.aedo.my_heaven.util.`object`.Constant.ALBUM_REQUEST_CODE
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.util.log.LLog
import com.aedo.my_heaven.util.log.LLog.TAG
import com.aedo.my_heaven.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate
import java.util.*


class MakeActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMakeBinding
    private lateinit var apiServices: APIService
    private var uriList: ArrayList<Uri> = ArrayList()
    private var mViewModel: MakeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make)
        mBinding.activity = this@MakeActivity
        apiServices = ApiUtils.apiService
        mBinding.viewModel = mViewModel

        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvMakeData.text = onlyDate.toString()

        inStatusBar()
        makeTop()
        setupSpinnerHandler()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.setIsMainNoticeViewed(false)
    }

    private fun setupSpinnerHandler() {
        mBinding.makeTxSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBinding.spinnerText.text = mBinding.makeTxSpinner.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        mBinding.makeTxSpinnerInfor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBinding.spinnerInfoTextTt.text = mBinding.makeTxSpinnerInfor.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun makeTop() {
        val person = resources.getStringArray(R.array.spinner_relationship)
        val place = resources.getStringArray(R.array.spinner_place)

        val perAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, person)
        val placeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, place)

        mBinding.makeTxSpinner.adapter = perAdapter
        mBinding.makeTxSpinnerInfor.adapter = placeAdapter

    }

    fun onOkClick(v: View) {
        val spinner_text = mBinding.spinnerText.text.toString()
        val make_person = mBinding.makeTxName.text.toString()
        val make_phone = mBinding.makeTxPhone.text.toString()
        val place = mBinding.spinnerInfoTextTt.text.toString()
        val deceased_name = mBinding.makeTxPersonName.text.toString()
        val deceased_age = mBinding.makeTxAge.text.toString()
        val eod_date = mBinding.eodText.text.toString()
        val eod_time = mBinding.eodTextTime.text.toString()
        val coffin_date = mBinding.coffinText.text.toString()
        val coffin_time = mBinding.coffinTextTime.text.toString()
        val dofp_date = mBinding.dofpText.text.toString()
        val dofp_time = mBinding.dofpTextTime.text.toString()
        val buried = mBinding.makeTxPlace.text.toString()
        val word = mBinding.makeTxTex.text.toString()

        when {
            spinner_text.isEmpty() -> {
                mBinding.spinnerText.error = "미입력"
            }
            make_person.isEmpty() -> {
                mBinding.makeTxName.error = "대미입력"
            }
            make_phone.isEmpty() -> {
                mBinding.makeTxPhone.error = "미입력"
            }
            place.isEmpty() -> {
                mBinding.spinnerInfoTextTt.error="미입력"
            }
            deceased_name.isEmpty() -> {
                mBinding.makeTxPersonName.error = "미입력"
            }
            deceased_age.isEmpty() -> {
                mBinding.makeTxAge.error = "미입력"
            }
            eod_date.isEmpty() -> {
                mBinding.eodText.error = "미입력"
            }
            eod_time.isEmpty() -> {
                mBinding.eodTextTime.error = "미입력"
            }
            coffin_date.isEmpty() -> {
                mBinding.coffinText.error = "미입력"
            }
            coffin_time.isEmpty() -> {
                mBinding.coffinTextTime.error = "미입력"
            }
            dofp_date.isEmpty() -> {
                mBinding.dofpText.error = "미입력"
            }
            dofp_time.isEmpty() -> {
                mBinding.dofpTextTime.error = "미입력"
            }
            word.isEmpty() -> {
                mBinding.makeTxTex.error = "미입력"
            }
            else -> {
                dialog?.show()
                callCreateAPI()
            }
        }
        finish()
    }

    private fun callCreateAPI() {

        val resident = Resident(mBinding.spinnerText.text.toString(),
            mBinding.makeTxName.text.toString(),mBinding.makeTxPhone.text.toString())

        val place = Place(mBinding.spinnerInfoTextTt.text.toString())

        val deceased = Deceased(mBinding.makeTxPersonName.text.toString(),
            mBinding.makeTxAge.text.toString())

        val eod = Eod(mBinding.eodText.text.toString(),mBinding.eodTextTime.text.toString())

        val coffin =Coffin(mBinding.coffinText.text.toString(),mBinding.coffinTextTime.text.toString())

        val dofp = Dofp(mBinding.dofpText.text.toString(),mBinding.dofpTextTime.text.toString())

        val buried = mBinding.makeTxPlace.text.toString()

        val word = mBinding.makeTxTex.text.toString()

         val created =mBinding.tvMakeData.text.toString()

        val data = CreateModel(resident,place,deceased,eod,coffin,dofp, buried, word, created)

        val mytoken = prefs.mytoken.toString()
        val bear = "Bearer"

        LLog.e("만들기_첫번째 API")
        apiServices.getCreate(prefs.myaccesstoken,data).enqueue(object : Callback<CreateModel> {
            override fun onResponse(call: Call<CreateModel>, response: Response<CreateModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"callCreateAPI API SUCCESS -> $result")
                    moveList()
                }
                else {
                    Log.d(TAG,"callCreateAPI API ERROR -> ${response.errorBody()}")
                    Log.d(TAG,"callCreateAPI API ERROR TWO-> $bear+$mytoken")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<CreateModel>, t: Throwable) {
                Log.d(TAG,"callCreate Fail -> $t")
                Log.d(TAG,"Bearer $mytoken")
                Toast.makeText(this@MakeActivity,"다시 시도해 주세요",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun otherAPI() {
        val resident = Resident(mBinding.spinnerText.text.toString(),
            mBinding.makeTxName.text.toString(),mBinding.makeTxPhone.text.toString())

        val place = Place(mBinding.spinnerInfoTextTt.text.toString())

        val deceased = Deceased(mBinding.makeTxPersonName.text.toString(),
            mBinding.makeTxAge.text.toString())

        val eod = Eod(mBinding.eodText.text.toString(),mBinding.eodTextTime.text.toString())

        val coffin =Coffin(mBinding.coffinText.text.toString(),mBinding.coffinTextTime.text.toString())

        val dofp = Dofp(mBinding.dofpText.text.toString(),mBinding.dofpTextTime.text.toString())

        val buried = mBinding.makeTxPlace.text.toString()

        val word = mBinding.makeTxTex.text.toString()

        val created =mBinding.tvMakeData.text.toString()

        val data = CreateModel(resident,place,deceased,eod,coffin,dofp, buried, word, created)

        val mytoken = prefs.mytoken.toString()
        val bear = "Bearer"

        LLog.e("만들기_두번째 API")
        apiServices.getCreate(prefs.newaccesstoken,data).enqueue(object : Callback<CreateModel> {
            override fun onResponse(call: Call<CreateModel>, response: Response<CreateModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"callCreateAPI Second API SUCCESS -> $result")
                    moveList()
                }
                else {
                    Log.d(TAG,"callCreateAPI Second API ERROR -> ${response.errorBody()}")
                    Log.d(TAG,"callCreateAPI Second API ERROR TWO-> $bear+$mytoken")
                }
            }

            override fun onFailure(call: Call<CreateModel>, t: Throwable) {
                Log.d(TAG,"callCreateAPI Second Fail -> $t")
                Log.d(TAG,"Bearer $mytoken")
                Toast.makeText(this@MakeActivity,"다시 시도해 주세요",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onBackClick(v: View){
        moveMain()
    }

    @SuppressLint("SetTextI18n")
    fun onEodDataClick(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance() //캘린더 뷰
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            mBinding.eodText.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onEodTimeClick(v:View) {
        var timeString = ""
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeString = "${hour}:${minute}"
            mBinding.eodTextTime.text = timeString
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    fun onCoffinDataClick(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance() //캘린더 뷰
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            mBinding.coffinText.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onCoffinTimeClick(v:View) {
        var timeString = ""
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeString = "${hour}:${minute}"
            mBinding.coffinTextTime.text = timeString
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    fun onDofpDataClick(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            mBinding.dofpText.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onDofpTimeClick(v:View) {
        var timeString = ""
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeString = "${hour}:${minute}"
            mBinding.dofpTextTime.text = timeString
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun onPickClick(v: View) {
        cameraPermission()
    }
    private fun cameraPermission() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,ALBUM_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode ==ALBUM_REQUEST_CODE) {
            uriList.clear()
            val ImnageData: Uri? = data?.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImnageData)
                mBinding.imgPake.setImageBitmap(bitmap)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}