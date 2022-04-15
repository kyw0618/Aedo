package com.aedo.my_heaven.api

import com.aedo.my_heaven.util.`object`.Constant
import com.aedo.my_heaven.util.`object`.Constant.BASE_URL_TEST

object ApiUtils {
    val apiService: APIService
    get() = RetrofitClient.getClient(BASE_URL_TEST).create(APIService::class.java)

}