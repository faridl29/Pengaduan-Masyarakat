package com.mfa.pengaduanmasyarakat.helpers

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mfa.pengaduanmasyarakat.BaseActivity
import com.mfa.pengaduanmasyarakat.BuildConfig
import com.mfa.pengaduanmasyarakat.MainActivity
import com.mfa.pengaduanmasyarakat.models.OnSuccessAutoload
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.lang.Exception

class PusherHelper: ConnectionEventListener {
    private lateinit var activity: Activity
    private lateinit var con: Context
    private lateinit var pusher: Pusher
    private lateinit var channel: Channel

    fun autoLoad(activity: Activity) {
        this.activity = activity
        this.con = activity
        val options = PusherOptions()
        options.setCluster(BuildConfig.PUSHER_CLUSTER);

        pusher = Pusher(BuildConfig.PUSHER_KEY, options)

        channel = pusher.subscribe("my-channel")
        checkConnection()
        onConnected()
    }

    private fun checkConnection(){
        Thread(Runnable {
            while (true) {
                // This string is displayed when device is not connected
                // to either of the aforementioned states
                var conStat: String = "Not Connected"

                // Invoking the Connectivity Manager
                val cm = activity.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

                // Fetching the Network Information
                val netInfo = cm.allNetworkInfo

                // Finding if Network Info typeName is WIFI or MOBILE (Constants)
                // If found, the conStat string is supplied WIFI or MOBILE DATA
                // respectively. The supplied data is a Variable
                for (ni in netInfo) {
                    if (ni.typeName.equals("WIFI", ignoreCase = true))
                        if (ni.isConnected) conStat = "WIFI"
                    if (ni.typeName.equals("MOBILE", ignoreCase = true))
                        if (ni.isConnected) conStat = "MOBILE DATA"
                }

                // To update the layout elements in real-time, use runOnUiThread method
                // We are setting the text in the TextView as the string conState
                activity.runOnUiThread {
                    if (conStat == "Not Connected"){
                        pusher.disconnect()
                    }else{
                        pusher.connect()
                    }
                }
            }
        }).start() // Starting the thread
    }

    private fun onConnected(){
        pusher.connection.bind(ConnectionState.ALL, this);

        channel.bind("admin-event") { event ->
            val jsonRESULTS = JSONObject(event.data)
            if(jsonRESULTS.getString("message") == "success"){
                (activity as MainActivity).showMessage(jsonRESULTS.getString("message"))
                Log.i("PusherAdmin","Received event with data: ${jsonRESULTS.getString("message")}")
            }
        }

        channel.bind("user-event") { event ->
            val jsonRESULTS = JSONObject(event.data)
            if(jsonRESULTS.getString("message") == "success"){
                Log.i("PusherUser","Received event with data: ${jsonRESULTS.getString("message")}")
            }
        }

        channel.bind("complaint-event") { event ->
            val jsonRESULTS = JSONObject(event.data)
            if(jsonRESULTS.getString("message") == "success"){
                Log.i("PusherComplaint","Received event with data: ${jsonRESULTS.getString("message")}")
            }
        }
    }

    override fun onConnectionStateChange(change: ConnectionStateChange?) {
        Log.i("Pusher", "State changed from ${change?.previousState} to ${change?.currentState}")
        val connection = change?.currentState.toString()
        if(connection.equals("CONNECTED") || connection.equals("DISCONNECTED")){
            (activity as BaseActivity).showSnackbar(connection)
        }
    }

    override fun onError(message: String?, code: String?, e: Exception?) {
        Log.i("Pusher", "There was a problem connecting! code ($code), message ($message), exception($e)")
    }
}