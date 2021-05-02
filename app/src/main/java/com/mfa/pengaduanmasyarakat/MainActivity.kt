package com.mfa.pengaduanmasyarakat

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.mfa.pengaduanmasyarakat.databinding.ActivityMainBinding
import com.mfa.pengaduanmasyarakat.helpers.CheckConnectivityHelper
import com.mfa.pengaduanmasyarakat.helpers.PusherHelper
import java.util.*


class MainActivity : BaseActivity() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var con = CheckConnectivityHelper()
    private var pusherHelper = PusherHelper()
    private lateinit var activityMainBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        pusherHelper.autoLoad(this)

        activityMainBinding.welcomeText.setOnClickListener {
            var intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }


    fun showMessage(message:String) {
        val currentTime: Date = Calendar.getInstance().getTime()
//        this.runOnUiThread(Runnable {
//            welcomeText.text = currentTime.toString()
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        })
//        handler = object : Handler(Looper.getMainLooper()) {
//
//        }
//        handler = Handler()
//        handler!!.post {
//                runOnUiThread{
//                        Runnable {
//                            welcomeText.text = currentTime.toString()
//                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                        }
//                }
//
//        }
        runOnUiThread{
            activityMainBinding.welcomeText.text = currentTime.toString()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    }
}