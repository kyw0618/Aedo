package com.example.my_heaven.api

import com.example.my_heaven.model.list.Condole
import com.example.my_heaven.model.list.ListDelete
import com.example.my_heaven.model.list.RecyclerList
import com.example.my_heaven.model.restapi.base.*
import com.example.my_heaven.model.restapi.login.*
import retrofit2.Call
import retrofit2.http.*

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

    //회원정보 API
    @GET("v1/user")
    fun getUser(@Header("Accesstoken")accesstoken: String?): Call<GetUser>

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
    fun getCreateName(@Header("Accesstoken")accesstoken: String?) : Call<CreateName>

    //나의부고 API
    @GET("v1/obituary/my")
    fun getCreateGet(@Header("Accesstoken")accesstoken: String?): Call<RecyclerList>

    //부고수정 API
    @PUT("v1/obituary/:id")
    fun getCreatePut(@Header("Accesstoken")accesstoken: String?)

    //부고삭제
    @DELETE("v1/obituary/:id")
    fun getCreateDelete(@Header("Accesstoken")accesstoken: String?,@Query("id")id: String?=null) : Call<ListDelete>

    //조문메세지 API
    @POST("v1/condole")
    fun getCondole(@Header("Accesstoken")accesstoken: String?, @Body createMessage: CreateMessage): Call<CreateMessage>

    //조문메세지 조회 API
    @GET("v1/condole/:id")
    fun getConID(@Query("id")id: String?=null,@Header("Accesstoken")accesstoken: String?) : Call<Condole>

    //조문메세지 수정 API
    @PUT("v1/condole/:id")
    fun getConPut(@Header("Accesstoken")accesstoken: String?)

    //조문메세지 삭제 API
    @DELETE("v1/condole/:id")
    fun getConDelete(@Header("Accesstoken")accesstoken: String?)

    //공지사항 모두조회 API
    @GET("v1/center/announcement")
    fun getNoti(@Header("Accesstoken")accesstoken: String?)

    //공지사항 세부조회 API
    @GET("v1/center/announcement/:id")
    fun getNotiDetail(@Header("Accesstoken")accesstoken: String?)

    //공지사항 작성
    @POST("v1/center/announcement")
    fun getNotiPost(@Header("Accesstoken")accesstoken: String?)

    //공지사항 수정
    @PUT("v1/center/announcement:id")
    fun getNotiPut(@Header("Accesstoken")accesstoken: String?)

    //공지사항 삭제
    @DELETE("v1/center/announcement:id")
    fun getNotiDelete(@Header("Accesstoken")accesstoken: String?)

}