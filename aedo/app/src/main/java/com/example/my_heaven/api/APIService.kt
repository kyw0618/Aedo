package com.example.my_heaven.api

import android.widget.EditText
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.model.restapi.base.*
import com.example.my_heaven.model.restapi.login.LogOut
import com.example.my_heaven.model.restapi.login.LoginResult
import com.example.my_heaven.model.restapi.login.LoginSMS
import com.example.my_heaven.model.restapi.login.LoginSend
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface APIService {
    //검증 API
    @GET("v1/app/verification")
    fun getVerification(@Header("abcd-ef")abcdef: String?): Call<Verification>

    //정책 API
    @GET("v1/app/policy")
    fun getPolicy(): Call<AppPolicy>

    //자동로그인 API
    @PUT("v1/user/auto")
    fun getautoLogin(@Header("Accesstoken")accesstoken: String?): Call<AutoLogin>

    //로그인 API
    @PUT("v1/user")
    fun getLogin(@Body loginSend: LoginSend):Call<LoginSend>

    //회원가입 API
    @POST("v1/user")
    fun getSignUp(@Body loginResult:LoginResult):Call<LoginResult>

    //문자인증 API
    @POST("v1/user/sms")
    fun getSMS(@Body loginSMS: LoginSMS): Call<LoginSMS>

    //약관보기 API
    @GET("v1/user/terms")
    fun getTerms(): Call<TremModel>

    //로그아웃 API
    @DELETE
    fun getLogOut(@Header("Accesstoken")accesstoken: String?): Call<LogOut>

    //부고작성 API
    @POST("v1/obituary")
    fun getCreate(@Header("Accesstoken")accesstoken: String?,@Body resident: CreateModel): Call<CreateModel>

    //부고조회 API
    @GET("v1/obituary?name=")
    fun getCreateName() : Call<CreateName>

    //나의부고 API
    @GET("v1/obituary/my")
    fun getCreateGet(@Header("Accesstoken")accesstoken: String?): Call<RecyclerList>

    //부고수정 API
    @PUT("v1/obituary/:id")
    fun getCreatePut()

    //부고삭제
    @DELETE("v1/obituary/:id")
    fun getCreateDelete()

    //조문메세지 API
    @GET("v1/condole")
    fun getCondole(@Body condole: Condole): Call<Condole>

    //조문메세지 조회 API
    @GET("v1/condole/:id")
    fun getConID() : Call<ConID>

    //조문메세지 수정 API
    @PUT("v1/condole/:id")
    fun getConPut()

    //조문메세지 삭제 API
    @DELETE("v1/condole/:id")
    fun getConDelete()

    //공지사항 모두조회 API
    @GET("v1/center/announcement")
    fun getNoti()

    //공지사항 세부조회 API
    @GET("v1/center/announcement/:id")
    fun getNotiDetail()

    //공지사항 작성
    @POST("v1/center/announcement")
    fun getNotiPost()

    //공지사항 수정
    @PUT("v1/center/announcement:id")
    fun getNotiPut()

    //공지사항 삭제
    @DELETE("v1/center/announcement:id")
    fun getNotiDelete()

}