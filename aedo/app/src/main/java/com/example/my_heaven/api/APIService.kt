package com.example.my_heaven.api

import android.widget.EditText
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.model.restapi.base.*
import com.example.my_heaven.model.restapi.login.LoginResult
import com.example.my_heaven.model.restapi.login.LoginSMS
import com.example.my_heaven.model.restapi.login.LoginSend
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface APIService {
    @GET("v1/app/verification")
    fun getVerification(@Header("abcd-ef")abcdef: String?): Call<Verification>

    @GET("v1/app/policy")
    fun getPolicy(@Header("abcd-ef")abcdef: String?): Call<AppPolicy>

    @POST("v1/user/login")
    fun getLogin(@Body loginSend: LoginSend):Call<LoginSend>

    @POST("v1/user/signup")
    fun getSignUp(@Body loginResult:LoginResult):Call<LoginResult>

    @POST("v1/user/sms")
    fun getSMS(@Body loginSMS: LoginSMS): Call<LoginSMS>

    @POST("v1/obituary/create")
    fun getCreate(@Body resident: CreateModel): Call<CreateModel>

    @GET("v1/obituary/myobituary")
    fun getCreateGet(): Call<RecyclerList>

}