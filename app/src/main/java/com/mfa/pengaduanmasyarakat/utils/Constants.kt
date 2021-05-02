package com.mfa.pengaduanmasyarakat.utils

import android.content.Context
import kotlinx.coroutines.CoroutineScope

class Constants {
    companion object{
        const val API_ENDPOINT = "http://192.168.43.198/pengaduan_masyarakat/public/api/"

        fun getToken(context: Context) : String {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            val token = pref.getString("TOKEN", "undefined")
            return token!!
        }

        fun setToken(context: Context, token: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply{
                putString("TOKEN", token)
                apply()
            }
        }

        fun clearToken(context: Context) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }
    }
}