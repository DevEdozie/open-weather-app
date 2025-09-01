package com.example.openweather.core


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject


class NetworkObserver @Inject constructor(
    private val context: Context
) {
    private val cm = context.getSystemService(ConnectivityManager::class.java)!!

    fun isOnline(): Boolean {
        val net = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(net) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}