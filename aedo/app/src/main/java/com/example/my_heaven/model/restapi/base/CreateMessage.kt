package com.example.my_heaven.model.restapi.base

import com.google.gson.annotations.SerializedName

data class CreateMessage(
    val title : String? = null, // 제목
    val content : String? = null,   // 내용
    val created : String? = null,   //만든 날짜
    val obId : String? = null   //부고ID
)
