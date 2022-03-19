package com.example.my_heaven.model.notice

data class NoticeModel(
    val announcement : List<Announcement>
)

data class Announcement(
    val id : String? = null,
    val title : String? = null,
    val value : String? = null,
    val time : String? = null
)