package com.mfa.pengaduanmasyarakat.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mfa.pengaduanmasyarakat.databinding.ActivityLoginBinding
import com.mfa.pengaduanmasyarakat.databinding.ActivityMainBinding
import com.mfa.pengaduanmasyarakat.utils.Constants
import com.mfa.pengaduanmasyarakat.view_models.UserState
import com.mfa.pengaduanmasyarakat.view_models.UserViewModel

class LoginActivity : AppCompatActivity() {
    
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var userViewModel : UserViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)
        
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })
        
        
        activityLoginBinding.btnSignin.setOnClickListener {
            val nik = activityLoginBinding.edtNik.text.toString().trim()
            val password = activityLoginBinding.edtPassword.text.toString().trim()
            if (userViewModel.validate(nik, password)){
                userViewModel.login(nik, password)
            }
        }
        
    }
    
    private fun handleUIState(it: UserState){
        when(it){
            is UserState.Reset -> {
                setNikError(null)
                setPasswordError(null)
            }
            is UserState.IsError -> {
                isLoading(false)
                toast(it.error)
            }
            is UserState.ShowToast -> {
                toast(it.message)
            }
            is UserState.IsFailed -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Validate -> {
                it.nik?.let {
                    setNikError(it)
                }
                it.password?.let {
                    setPasswordError(it)
                }
            }
            is UserState.IsSuccess -> {
                Constants.setToken(this, it.token)
                startActivity(Intent(this, ComplaintActivity::class.java)).also {
                    finish()
                }
            }
            is UserState.IsLoading -> {
                isLoading(it.state)
            }
        }
    }

    private fun setNikError(err: String?){
        activityLoginBinding.edtNik.error = err
    }

    private fun setPasswordError(err: String?){
        activityLoginBinding.edtPassword.error = err
    }

    private fun isLoading(state: Boolean){
        if (state){
            activityLoginBinding.btnSignin.isEnabled = false
            activityLoginBinding.btnSignin.startAnimation()
        } else {
            activityLoginBinding.btnSignin.isEnabled = true
            activityLoginBinding.btnSignin.revertAnimation()
        }
    }

    private fun toast(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}