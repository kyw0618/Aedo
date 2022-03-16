package com.example.my_heaven.model.restapi.login

import io.realm.mongodb.User

data class GetUser(
    val user : UserGet? = null
)

data class UserGet(
    val id : String? = null,
    val phone : String? = null,
    val birth : String? = null,
    val name : String? = null,
    val terms : String? = null
)
