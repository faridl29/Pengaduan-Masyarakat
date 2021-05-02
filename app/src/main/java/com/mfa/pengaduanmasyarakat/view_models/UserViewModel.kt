package com.mfa.pengaduanmasyarakat.view_models

import androidx.lifecycle.ViewModel
import com.mfa.pengaduanmasyarakat.models.User
import com.mfa.pengaduanmasyarakat.utils.SingleLiveEvent
import com.mfa.pengaduanmasyarakat.utils.WrappedResponse
import com.mfa.pengaduanmasyarakat.web_services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()
    
    fun login(nik: String, password: String){
        state.value = UserState.IsLoading(true)
        api.login(nik, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    var body = response.body() as WrappedResponse<User>
                    if (body.status.equals("200")){
                        state.value = UserState.IsSuccess("Bearer ${body.data!!.api_token}")
                    }else {
                        state.value = UserState.IsFailed("Login Gagal")
                    }
                }else {
                    state.value = UserState.IsError("Kesalahan terjadi saat login")
                }
                state.value = UserState.IsLoading(false)
            }
    
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                state.value = UserState.IsError(t.message)
            }
    
        })
    }

    fun validate(nik: String, password: String) : Boolean {
        state.value = UserState.Reset
        if (nik.isEmpty()){
            state.value = UserState.Validate(nik = "NIK tidak boleh kosong")
            return false
        }

        if (nik.length < 16){
            state.value = UserState.Validate(nik = "NIK kurang dari 16 karakter")
            return false
        }

        if (password.isEmpty()){
            state.value = UserState.Validate(password = "Password tidak boleh kosong")
            return false
        }
        return true
    }

    fun getState() = state
}

sealed class UserState() {
    data class IsError(var error: String?) : UserState()
    data class ShowToast(var message: String?) : UserState()
    data class Validate(
        var nik: String? = null,
        var password: String? = null
    ) : UserState()
    data class IsLoading(var state: Boolean = false) : UserState()
    data class IsSuccess(var token: String) : UserState()
    data class IsFailed(var message: String) :UserState()
    object Reset: UserState()
}