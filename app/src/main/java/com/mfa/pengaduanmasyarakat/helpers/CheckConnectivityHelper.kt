package com.mfa.pengaduanmasyarakat.helpers

import android.content.Context
import android.net.ConnectivityManager


class CheckConnectivityHelper {
    fun isConnected(con : Context) : Boolean {
        val connectivityManager: ConnectivityManager
        connectivityManager =
            con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        assert(connectivityManager != null)
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}