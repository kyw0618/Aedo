package com.aedo.my_heaven.api

import com.aedo.my_heaven.util.`object`.Constant

object ApiUtils {
    val apiService: APIService
    get() = RetrofitClient.getClient(Constant.BASE_URL_TEST).create(APIService::class.java)

}