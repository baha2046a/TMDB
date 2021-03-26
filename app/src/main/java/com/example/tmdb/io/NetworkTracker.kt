package com.example.tmdb.io

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.util.Log

/**
 *   Track Internet Connection in Android (API Level 29) Using Network Callback
 */
object NetworkTracker {
    /** This value represent the number of network connection the device currently has
     *  e.g. MobileNetwork(1) Wifi(1) = 2
     *  zero means no network connection
     */
    var availableNetworkCount: Int = 0
        private set

    val isAvailable: Boolean
        get() = availableNetworkCount > 0

    private val callback: NetworkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            availableNetworkCount += 1
            Log.d("CheckNetwork", "A $network")
        }

        override fun onLost(network: Network) {
            availableNetworkCount -= 1
            Log.d("CheckNetwork", "L $network")
        }
    }

    fun register(context: Context) {
        availableNetworkCount = 0
        Log.i("CheckNetwork", "[Status] registerCallback")
        kotlin.runCatching {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivityManager?.registerDefaultNetworkCallback(callback)
        }.onFailure {
            Log.e("CheckNetwork", "[Error] registerNetworkCallback : ${it.message}")
        }
    }

    fun unregister(context: Context) {
        Log.i("CheckNetwork", "[Status] unregisterCallback")
        kotlin.runCatching {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivityManager?.unregisterNetworkCallback(callback)
        }.onFailure {
            Log.e("CheckNetwork", "[Error] unRegisterNetworkCallback : ${it.message}")
        }
    }
}