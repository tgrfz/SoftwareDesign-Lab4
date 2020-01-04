package com.example.softwaredesign_lab4.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val title: String,
    val content: String,
    val date: String,
    val image: String,
    val link:String,
    var cachedImage: String = ""
) : Parcelable