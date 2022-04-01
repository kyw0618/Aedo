package com.aedo.my_heaven.model.list

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import retrofit2.http.Url
import java.io.File
import java.net.URI

data class ListImg(
    val imgName : Bitmap? = null
)

