package com.mfa.pengaduanmasyarakat

import android.R
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mfa.pengaduanmasyarakat.utils.GlobalFunction


open class BaseActivity : AppCompatActivity() {
    var mTextViewScreenTitle: TextView? = null
    var mImageButtonBack: ImageButton? = null
    var mProgressDialog: ProgressDialog? = null
    var globalFunction = GlobalFunction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mProgressDialog = ProgressDialog(this);
        mProgressDialog?.setMessage("Loading");
        mProgressDialog?.setCancelable(false);
        mProgressDialog?.setIndeterminate(true);
    }

    fun showSnackbar(connection: String){
        if(connection.equals("CONNECTED")){
            runOnUiThread {
                globalFunction.createSnackbar(this, getRootView()!!, connection, "success")
                Toast.makeText(this, connection, Toast.LENGTH_SHORT).show()
            }
        }else if(connection.equals("DISCONNECTED")){
            runOnUiThread {
                globalFunction.createSnackbar(this, getRootView()!!, connection, "error")
                Toast.makeText(this, connection, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setScreenTitle(resId:Int) {
        mTextViewScreenTitle?.setText(resId);
    }

    fun setScreenTitle(title:String) {
        mTextViewScreenTitle?.setText(title);
    }

    fun getBackButton(): ImageButton? {
        return mImageButtonBack;
    }

    fun showProgressDialog() {
        if (!mProgressDialog?.isShowing()!!) {
            mProgressDialog?.show();
        }
    }

    fun dismissProgressDialog() {
        if (mProgressDialog?.isShowing()!!) {
            mProgressDialog?.dismiss();
        }
    }

    private fun getRootView(): View? {
        val contentViewGroup = findViewById<View>(R.id.content) as ViewGroup
        var rootView: View? = null
        if (contentViewGroup != null) rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = window.decorView.rootView
        return rootView
    }
}