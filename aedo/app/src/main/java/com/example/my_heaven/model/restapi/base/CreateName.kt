package com.example.my_heaven.model.restapi.base

data class CreateName(
    val search : List<CreateSearch>?=null
)

data class CreateSearch(
    val resident: String? = null,
    val deceased: String? = null,
    val place : String? = null
)
