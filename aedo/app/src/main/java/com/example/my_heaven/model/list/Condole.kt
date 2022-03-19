package com.example.my_heaven.model.list

data class Condole (
    var condole : List<CondoleList>? = null
)

data class CondoleList(
    var id: String?=null,
    var title : Title? = null,
    var content : Content? = null,
    var created : Created? = null,
    var obld : Obld? = null
)

data class Title(
    var title : String? = null
)

data class Content(
    var content : String? = null
)

data class Created(
    var created : String? = null
)

data class Obld(
    var obld : String? = null
)


