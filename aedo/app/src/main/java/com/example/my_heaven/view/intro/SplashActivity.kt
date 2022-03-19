package com.example.my_heaven.view.intro

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.my_heaven.R
import com.example.my_heaven.api.APIService
import com.example.my_heaven.api.ApiUtils
import com.example.my_heaven.databinding.ActivitySplashBinding
import com.example.my_heaven.model.restapi.base.*
import com.example.my_heaven.util.`object`.ActivityControlManager
import com.example.my_heaven.util.`object`.Constant
import com.example.my_heaven.util.`object`.Constant.PREF_ACCESS_TOKEN
import com.example.my_heaven.util.`object`.Constant.RESULT_TRUE
import com.example.my_heaven.util.alert.CustomDialog
import com.example.my_heaven.util.base.BaseActivity
import com.example.my_heaven.util.base.MyApplication
import com.example.my_heaven.util.log.LLog
import com.example.my_heaven.util.log.LLog.TAG
import com.example.my_heaven.util.network.ResultListener
import com.example.my_heaven.util.root.RootUtil
import com.example.my_heaven.util.style.TextStyle
import com.example.my_heaven.view.login.LoginActivity
import com.example.my_heaven.view.main.MainActivity
import com.getkeepsafe.relinker.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var apiServices: APIService
    private var devpolicyversion: String? = null
    private var prefs = MyApplication.prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiServices = ApiUtils.apiService
        inStatusBar()
        checknetwork()
//        moveActivity()
    }

    // 네트워크 체크
    private fun checknetwork() {
        LLog.e("1. 네트워크 확인")
        if (!isNetworkAvailable) {
            networkDialog()
            return
        }
//        checkLoot()
        checkVerification()
    }

    // 2. 루팅 확인
    private fun checkLoot() {
        LLog.e("2. 루팅 확인")
        if (!BuildConfig.DEBUG && RootUtil.isDeviceRooted) {
            rootingDialog()
            return
        }
    }

    // 3. 검증 API 호출키 및 Hash키 검증
    private fun checkVerification() {
        LLog.e("3. 검증 API 호출키 및 Hash키 검증")
        requestVerificationJson(object : ResultListener {
            override fun onSuccess() {
                checkPolicyVersion()
            }
        })
    }

    // 4. 정책 버전 비교 및 저장
    private fun checkPolicyVersion() {
        LLog.e("4. 정책 버전 비교 및 저장")
        requestPolicy(object : ResultListener {
            override fun onSuccess() {
                enablecheck()
            }
        })
    }

    private fun requestVerificationJson(listener: ResultListener) {
        LLog.e("APP HASH: $hash")
        val vercall: Call<Verification> = apiServices.getVerification("qwer")
        vercall.enqueue(object : Callback<Verification> {
            override fun onResponse(call: Call<Verification>, response: Response<Verification>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    val encrypt = Encrypt()
                    encrypt.key.toString()
                    encrypt.iv.toString()
                    result.result
                    result.encrypt
                    result.policy_ver
                    Log.d(TAG,"Vertification result SUCESS -> $result")
                    if (result.result == RESULT_TRUE) {
                        devpolicyversion = result.policy_ver.toString()
                        getinformation(result, listener)
                    }
                    else {
                        Log.d(TAG,"Vertification result ERROR -> ${result.result.equals("true")}")
                        alert?.showDialog(
                            getString(R.string.warning_repackaging)) {
                            finishAffinity()
                        }?.cancelable(false)
                    }
                }
                else {
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<Verification>, t: Throwable) {
                Log.d(TAG, "loadVerAPI error -> $t")
                serverDialog()
            }
        })
    }

    private fun getinformation(result: Verification?, networkListener: ResultListener) {
        val aes_iv: String? = Encrypt().iv
        val aes_key: String? = Encrypt().key
        prefs.myeniv = aes_iv
        prefs.myenkey = aes_key
        prefs.mylangcode = "LANG_0001"
        prefs.myhashKey = hash.toString()
        Log.d(TAG,"Prefs AppToken SUCCESS -> $hash")
        Log.d(TAG,"Prefs MyEnIv SUCCESS -> ${prefs.myeniv}")
        Log.d(TAG,"Prefs MyEnKey SUCCESS -> ${prefs.myenkey}")
        networkListener.onSuccess()
    }

    private fun requestPolicy(listener: ResultListener) {
        val vercall: Call<AppPolicy> = apiServices.getPolicy()
        vercall.enqueue(object : Callback<AppPolicy> {
            override fun onResponse(call: Call<AppPolicy>, response: Response<AppPolicy>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Policy response SUCCESS -> $result")
                    realmPolicy(result,listener)
                    requestLogin()
                }
                else {
                    Log.d(TAG,"Policy response ERROR -> $result")
                    serverDialog()
                }
            }
            override fun onFailure(call: Call<AppPolicy>, t: Throwable) {
                Log.d(TAG, "Policy error -> $t")
                serverDialog()
            }
        })
    }

    private fun realmPolicy(result: AppPolicy, listener: ResultListener) {
        realm.executeTransaction {
            realm.where(Policy::class.java).findAll().deleteAllFromRealm()
            realm.where(Code::class.java).findAll().deleteAllFromRealm()
            realm.where(AppMenu::class.java).findAll().deleteAllFromRealm()
            realm.where(Coordinates::class.java).findAll().deleteAllFromRealm()
            realm.copyToRealm(result.policy!!)
            realm.copyToRealm(result.code!!)
            realm.copyToRealm(result.app_menu!!)
            realm.copyToRealm(result.coordinates!!)
        }

    }

    private fun requestLogin() {
        val vercall: Call<AutoLogin> = apiServices.getautoLogin(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<AutoLogin> {
            override fun onResponse(call: Call<AutoLogin>, response: Response<AutoLogin>) {
                val result = response.body()
                if (response.code() == 404 || response.code() == 401) {
                    moveLogin()
                }
                else if(response.code() == 200){
                    getPreferences(0).edit().remove("PREF_ACCESS_TOKEN").apply()
                    prefs.newaccesstoken=result?.accesstoken
                    moveMain()
                    Toast.makeText(this@SplashActivity,"자동로그인이 되었습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AutoLogin>, t: Throwable) {
                Log.d(TAG, "requestLogin error -> $t")
                serverDialog()
            }
        })
    }

    private fun enablecheck() {
        val useYn = realm.where(Policy::class.java).equalTo("id","APP_ENABLE_YN").findFirst()
        val popupText = realm.where(Policy::class.java).equalTo("id","APP_ENABLE_CONTENT").findFirst()
        if (useYn != null) {
            if (useYn.value.equals("Y")) {
                serverDialog()
                return
            }
        } else {
            serverDialog()
            return
        }
        emergencyPopup()
        checkAppVersion()
    }

    // App 버전 체크
    private fun checkAppVersion() {
        val versionCode: Policy? =
            realm.where(Policy::class.java).equalTo("id", "APP_VER_ANDROID").findFirst()
        if (versionCode != null) {
            val refrenceCode: Int = versionCode.value!!.toInt()
            var currentCode: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                currentCode = try {
                    packageManager.getPackageInfo(
                        this.packageName,
                        PackageManager.GET_ACTIVITIES
                    ).longVersionCode
                        .toInt()
                } catch (e: PackageManager.NameNotFoundException) {
                    -1
                }
            } else {
                val manager = this.packageManager
                val info: PackageInfo
                try {
                    info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
                    currentCode = info.versionCode
                } catch (e: PackageManager.NameNotFoundException) {
                    currentCode = -1
                }
            }
            if (currentCode < refrenceCode) {
                showAppUpdate()
                return
            }
        } else {
            serverDialog()
            return
        }
        emergencyPopup()
    }

    private fun showAppUpdate() {
        val upadateDialog = CustomDialog(this)
        upadateDialog.text(getString(R.string.need_update_app))
            ?.positive(getString(R.string.update)) { v ->
                val appPackageName =
                    packageName
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                    finish()
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                    finish()
                }
            }
    }

    // 긴급공지 체크
    private fun emergencyPopup() {
        val popupTime: Policy? =
            realm.where(Policy::class.java).equalTo("id","POPUP_TIME_END").findFirst()
        val popupContent: Policy? =
            realm.where(Policy::class.java).equalTo("id", "POPUP_CONTENT").findFirst()
        val popupEnable: Policy? =
            realm.where(Policy::class.java).equalTo("id", "POPUP_ENABLE_YN").findFirst()

        if (popupEnable != null) {
            if ("Y" == popupEnable.value && TextStyle.compareDateAvailable(
                    popupTime!!.value!!,
                    "yyyyMMddHHmmss"
                ) && !prefs.getStr(Constant.PREF_EMERGENCY_NOTICE_NOT_SHOW, "").equals(popupTime.value)
            ) {
                val dialog = CustomDialog(this)
                if (popupContent != null) {
                    dialog.text(popupContent.value)
                        ?.positive(getString(R.string.ok)) {
                            dialog.dismiss()
                        }
                        ?.negative(getString(R.string.not_show_again)) {
                            dialog.dismiss()
                            prefs.setStr(Constant.PREF_EMERGENCY_NOTICE_NOT_SHOW, popupTime.value)
                        }!!.cancelable(false).show()
                }
                return
            }
        }
    }
    private fun moveLogin() {
        ActivityControlManager.delayRun({
            moveAndFinishActivity(LoginActivity::class.java) },
            Constant.SPLASH_WAIT
        )
    }
}