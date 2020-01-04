package com.example.softwaredesign_lab4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import java.util.*

class NetworkState {
    companion object {
        fun getNetworkStatus(context: Context?): Boolean {
            val cm =
                context?.getSystemService((Context.CONNECTIVITY_SERVICE)) as ConnectivityManager
            val activeNetwork = Objects.requireNonNull(cm).activeNetwork
            activeNetwork?.let {
                return true
            } ?: return false
        }
    }
}