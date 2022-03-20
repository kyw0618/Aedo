package com.aedo.my_heaven.model.restapi.base

data class CreateName(
    val result : List<CreateSearch>?=null
)

data class CreateSearch(
    var id: String? = null,
    var resident : SResident?=null,
    var place : SPlace?=null,
    var deceased : SDeceased?=null,
    var eod : SEod?=null,
    var coffin : SCoffin?=null,
    var dofp : SDofp?=null,
    var buried : String?=null,
    var word : String?=null
)


data class SResident(
    var relation : String?=null,
    var name : String?=null,
    var phone : String?=null
)


data class SPlace(
    var place_name : String?=null,
)

data class SDeceased(
    var name: String?=null,
    var age: String?=null
)

data class SEod(
    var date: String?=null,
    var time: String?=null
)

data class SCoffin(
    var date: String?=null,
    var time: String?=null
)


data class SDofp(
    var date: String?=null,
    var time: String?=null
)
