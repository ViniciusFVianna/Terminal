package br.com.sudosu.terminal.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object AndroidUtil {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
            .map { connectivity.getNetworkInfo(it) }
            .any { it.state == NetworkInfo.State.CONNECTED };
    }
}