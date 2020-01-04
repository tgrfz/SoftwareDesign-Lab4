package com.example.softwaredesign_lab4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    val title: String,
    val content: String,
    val date: String,
    val image: String,
    @PrimaryKey
    val link: String,
    var cachedImage: String = ""
) {
    constructor() : this("", "", "", "", "")
}
