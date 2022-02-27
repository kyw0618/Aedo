package com.example.my_heaven.model.restapi.login

data class LoginResult (
    var phone : String?=null,
    var birth : String?=null,
    var name : String?=null,
    val term : String?=null
)

