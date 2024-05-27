package com.sobolevkir.playlistmaker.common.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

val Context.isNetworkConnected: Boolean
    get() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.getNetworkCapabilities(manager.activeNetwork)?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } ?: false
    }