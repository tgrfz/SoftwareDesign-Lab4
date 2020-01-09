package com.example.softwaredesign_lab4

import android.app.Application

class App : Application() {

    val storage by lazy { DataStorage(applicationContext) }
}