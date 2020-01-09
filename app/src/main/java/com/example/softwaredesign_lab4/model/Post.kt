package com.example.softwaredesign_lab4.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    var title: String,
    var content: String,
    var date: String,
    var image: String,
    @PrimaryKey
    var link: String,
    var cachedImage: String = ""
) {
    constructor() : this("", "", "", "", "")
}
