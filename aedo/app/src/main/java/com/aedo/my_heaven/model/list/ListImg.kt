package com.aedo.my_heaven.model.list

import com.google.gson.annotations.SerializedName
import java.net.URI

data class ListImg(
    val imgName: String? = null,
    var resident: sResident? = null,
    var place: String? = null,
    var deceased: sDeceased? = null,
    var eod: String? = null,
    var coffin: String? = null,
    var dofp: String? = null,
    var buried: String? = null,
    var word: String? = null,
    var userid : String?=null,
    var created: String? = null,
    @SerializedName("id")
    val ids : String?=null
)

data class sResident (
    var relation : String? = null,
    var name : String? = null,
    var phone : String? = null
)

data class sDeceased(
    var name: String? = null,
    var age: String? = null,
)
