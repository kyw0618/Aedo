package com.example.my_heaven.model.restapi.login

import com.google.gson.annotations.SerializedName

data class LoginResult (
    var phone : String?=null,
    var birth : String?=null,
    var name : String?=null,
    val term : String?=null,
    @SerializedName("Accesstoken")
    val accesstoken : String?=null
)

