package com.example.my_heaven.util.`object`

import android.Manifest
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import java.util.ArrayList

object  Constant {
    const val TAG = "My_Heaven"
    const val BASE_URL="http://49.50.165.23:8080/"
    const val PREF_KEY_USER_TOKEN = "PREF_KEY_USER_TOKEN"
    const val PREF_KEY_APP_TOKEN = "myAppToken"
    const val PREF_KEY_ENCTYPT_IV = "myEncryptIv"
    const val PREF_KEY_ENCTYPT_KEY = "myEncryptKey"
    const val PREF_KEY_AUTH_TOKEN ="myAuthToken"
    const val PREF_KEY_LANG = "myLang"
    const val PREF_KEY_LANG_CODE = "myLangCode"
    const val PREF_KEY_TOKEN = "myTOKEN"
    const val RESULT_TRUE = "true"
    const val PREF_PERMISSION_GRANTED = "PREF_PERMISSION_GRANTED"
    const val PREF_PHONE = "myphone"
    const val PREF_EMERGENCY_NOTICE_NOT_SHOW = "PREF_EMERGENCY_NOTICE_NOT_SHOW"
    const val PREF_LOGIN_YN = "PREF_LOGIN_YN"
    const val PREF_AUTO_LOGIN = "PREF_AUTO_LOGIN"
    const val PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE"
    const val PREF_KEY_LANGUAGE_CODE = "PREF_KEY_LANGUAGE_CODE"
    const val PREF_SMS = "PREF_SMS"
    const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
    const val PREF_NEW_ACCESS_TOKEN = "PREF_NEW_ACCESS_TOKEN"
    const val PREF_HASH_KEY = "PREF_HASH_KEY"
    const val PREF_LIST_ID = "PREF_LIST_ID"

    const val PREF_KEY_ENC_KEY = "PREF_KEY_ENC_KEY"
    const val PREF_KEY_ENC_IV = "PREF_KEY_ENC_IV"

    const val ONE_PERMISSION_REQUEST_CODE = 1
    const val ALL_PERMISSION_REQUEST_CODE = 2

    const val PROGRESS_TIMEOUT = 15000
    const val BACKPRESS_CLOSE_TIME = 1500

    var phoneAuthNum: String = ""
    var tel: String = ""
    const val headerKey: String = ""

    private val notrhEast = LatLng(39.788312, 132.893671)
    private val southWest = LatLng(30.664915, 122.628502)

    const val DEFATULT_TIMEOUT = 20000

    const val RESIDENT_NAME = "RESIDENT_NAME"
    const val RESIDENT_RELATION = "RESIDENT_RELATION"
    const val RESIDENT_PHONE = "RESIDENT_PHONE"

    const val PLACE_NAME = "PLACE_NAME"

    const val DECEASED_NAME = "DECEASED_NAME"
    const val DECEASED_AGE = "DECEASED_AGE"

    const val LIST_ID = "LIST_ID"

    const val EOD_NAME = "EOD_NAME"
    const val EOD_DATE = "EOD_DATE"

    const val COFFIN_DATE = "COFFIN_DATE"
    const val COFFIN_TIME = "COFFIN_TIME"

    const val DOFP_DATE = "DOFP_DATE"
    const val DOFP_TIME = "DOFP_TIME"

    const val BURIED = "BURIED"

    // Splash
    const val SPLASH_WAIT = 2500
    val MUTILE_PERMISSION: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
        , Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun delayRun(r: Runnable?, delay: Int) {
        val loop = Looper.myLooper()
        if (loop != null) {
            val handler = Handler(loop)
            handler.postDelayed(r!!, delay.toLong())
        }
    }




}