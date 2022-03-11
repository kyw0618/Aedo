package com.example.my_heaven.view.main.detail.make

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivityMakeBinding
import com.example.my_heaven.model.firebase.ItemDTO
import com.example.my_heaven.model.restapi.base.*
import com.example.my_heaven.util.`object`.Constant.BURIED
import com.example.my_heaven.util.`object`.Constant.COFFIN_DATE
import com.example.my_heaven.util.`object`.Constant.DECEASED_NAME
import com.example.my_heaven.util.`object`.Constant.DOFP_DATE
import com.example.my_heaven.util.`object`.Constant.EOD_DATE
import com.example.my_heaven.util.`object`.Constant.PLACE_NAME
import com.example.my_heaven.util.`object`.Constant.RESIDENT_NAME
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.base.MyApplication.Companion.prefs
import com.example.my_heaven.util.firebase.FirebaseViewModel
import com.example.my_heaven.util.log.LLog.TAG
import com.example.my_heaven.view.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MakeActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMakeBinding
    private lateinit var apiServices: APIService
    private var mViewModel: MakeViewModel? = null
    private var itemDTO = ItemDTO()
    private val firebaseViewModel : FirebaseViewModel by viewModels()
    private var valid = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make)
        mBinding.activity = this@MakeActivity
        apiServices = ApiUtils.apiService
        mBinding.viewModel = mViewModel
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

        when {
            spinner_text.isEmpty() -> {
                mBinding.spinnerText.error = "관계를 선택해 주세요"
            }
            make_person.isEmpty() -> {
                mBinding.makeTxName.error = "대표 상주 이름을 입력해 주세요"
            }
            make_phone.isEmpty() -> {
                mBinding.makeTxPhone.error = "대표 상주 번호를 입력해 주세요"
            }
            place.isEmpty() -> {
                mBinding.spinnerInfoTextTt.error="장례식장을 선택해주세요"
            }
            deceased_name.isEmpty() -> {
                mBinding.makeTxPersonName.error = "고인의 성함을 입력해 주세요"
            }
            deceased_age.isEmpty() -> {
                mBinding.makeTxAge.error = "고인의 나이를 입력해주세요"
            }
            eod_date.isEmpty() -> {
                mBinding.eodText.error = "임종 날짜를 선택해 주세요"
            }
            eod_time.isEmpty() -> {
                mBinding.eodTextTime.error = "임종 시간을 선택해 주세요"
            }
            coffin_date.isEmpty() -> {
                mBinding.coffinText.error = "입관 날짜를 선택해 주세요"
            }
            coffin_time.isEmpty() -> {
                mBinding.coffinTextTime.error = "입관 시간을 선택해 주세요"
            }
            dofp_date.isEmpty() -> {
                mBinding.dofpText.error = "발인 날짜를 선택해 주세요"
            }
            dofp_time.isEmpty() -> {
                mBinding.dofpTextTime.error = "발인 시간을 선택해 주세요"
            }
            else -> {

                dialog?.show()
                callCreateAPI()
                uploadItem(spinner_text,make_person,make_phone,place,deceased_age,deceased_name,eod_date,eod_time,coffin_date,coffin_time,dofp_date,dofp_time,buried)
            }
        }
        addList()
    }

    private fun addList() {
        val resultIntent = Intent()

        val deceased = mBinding.makeTxPersonName.text.toString()
        val eod_data = mBinding.eodText.text.toString()
        val resident = mBinding.makeTxName.text.toString()
        val place = mBinding.spinnerInfoTextTt.text.toString()
        val coffin = mBinding.coffinText.text.toString()
        val dofp = mBinding.dofpText.text.toString()
        val buried = mBinding.makeTxPlace.text.toString()

        resultIntent.putExtra(DECEASED_NAME,deceased)
        resultIntent.putExtra(EOD_DATE,eod_data)
        resultIntent.putExtra(RESIDENT_NAME,resident)
        resultIntent.putExtra(PLACE_NAME,place)
        resultIntent.putExtra(COFFIN_DATE,coffin)
        resultIntent.putExtra(DOFP_DATE,dofp)
        resultIntent.putExtra(BURIED,buried)
        setResult(Activity.RESULT_OK, resultIntent)

    }

    private fun uploadItem(
        spinnerText: String,
        makePerson: String,
        makePhone: String,
        place: String,
        deceasedAge: String,
        deceasedName: String,
        eodDate: String,
        eodTime: String,
        coffinDate: String,
        coffinTime: String,
        dofpDate: String,
        dofpTime: String,
        buried: String
    ) {

        val time = System.currentTimeMillis()
        itemDTO.spinner_text = spinnerText
        itemDTO.make_person = makePerson
        itemDTO.make_phone = makePhone
        itemDTO.place = place
        itemDTO.deceased_age = deceasedAge
        itemDTO.deceased_name = deceasedName
        itemDTO.eod_date = eodDate
        itemDTO.eod_time = eodTime
        itemDTO.coffin_date = coffinDate
        itemDTO.coffin_time = coffinTime
        itemDTO.dofp_date = dofpDate
        itemDTO.dofp_time = dofpTime
        itemDTO.timestamp = time
        itemDTO.buried = buried
        firebaseViewModel.uploadItem(itemDTO)
        dialog?.dismiss()
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

        val data = CreateModel(resident,place,deceased,eod,coffin,dofp, buried, word)

        val mytoken = prefs.mytoken.toString()
        val bear = "Bearer"

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

                }
            }

            override fun onFailure(call: Call<CreateModel>, t: Throwable) {
                Log.d(TAG,"callCreate ERROR -> $t")
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

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}