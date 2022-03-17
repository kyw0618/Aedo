package com.example.my_heaven.model.restapi.base

data class CreateMessage(
    val title : Title? = null,
    val content : Content? = null,
    val created : Created? = null,
    val obld : Obld? = null
)

data class Title(
    val title : String? = null
)

data class Content(
    val content : String? = null
)

data class Created(
    val created : String? = null
)

data class Obld(
    val obld : String? = null
)
