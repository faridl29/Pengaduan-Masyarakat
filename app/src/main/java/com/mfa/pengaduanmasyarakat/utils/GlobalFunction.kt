package com.mfa.pengaduanmasyarakat.utils

import android.R
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class GlobalFunction {
    fun createSnackbar(con: Context, view: View, s: String, type: String) {
        val snack = Snackbar.make(view, s, if(type == "error") Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_SHORT)
        val sbview = snack.view

        if(type == "error"){
            sbview.setBackgroundColor(ContextCompat.getColor(con, R.color.holo_red_light))
        }else if(type == "warning"){
            sbview.setBackgroundColor(ContextCompat.getColor(con, R.color.darker_gray))
        }else if(type == "success"){
            sbview.setBackgroundColor(ContextCompat.getColor(con, R.color.holo_green_light))
        }else{
            sbview.setBackgroundColor(ContextCompat.getColor(con, R.color.holo_blue_light))
        }

        var params = sbview.layoutParams
        if (params is WindowManager.LayoutParams) {
            params = sbview.layoutParams as WindowManager.LayoutParams
            params.gravity = Gravity.TOP
        }else if(params is FrameLayout.LayoutParams){
            params = sbview.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
        }

//        view.layoutParams = params
        snack.show()
    }
}