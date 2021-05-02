package com.mfa.pengaduanmasyarakat.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfa.pengaduanmasyarakat.models.Complaint
import com.mfa.pengaduanmasyarakat.utils.SingleLiveEvent
import com.mfa.pengaduanmasyarakat.utils.WrappedListResponse
import com.mfa.pengaduanmasyarakat.utils.WrappedResponse
import com.mfa.pengaduanmasyarakat.web_services.ApiClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ComplaintViewModel : ViewModel() {
    private var complaints = MutableLiveData<List<Complaint>>()
    private var complaint = MutableLiveData<Complaint>()
    private var state: SingleLiveEvent<ComplaintState> = SingleLiveEvent()
    private var api = ApiClient.instance()
    
    fun fetchAllComplaint(token: String){
        state.value = ComplaintState.IsLoading(true)
        api.allComplaint(token).enqueue(object : Callback<WrappedListResponse<Complaint>>{
            override fun onResponse(call: Call<WrappedListResponse<Complaint>>, response: Response<WrappedListResponse<Complaint>>) {
                if (response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Complaint>
                    if (body.status.equals("200")){
                        val data = body.data
                        complaints.postValue(data)
                    }else if(body.status.equals("401")){
                        state.value = ComplaintState.NotAuthenticated
                    }
                }else {
                    state.value = ComplaintState.IsError("Something went wrong")
                }
                state.value = ComplaintState.IsLoading(false)
            }

            override fun onFailure(call: Call<WrappedListResponse<Complaint>>, t: Throwable) {
                state.value = ComplaintState.IsError(t.message)
            }

        })
    }
    
    fun create(photo: String?, complaint: String?, category: String?){
        state.value = ComplaintState.IsLoading(true)
    
        val file = File(photo)
    
        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                  file
        )
    
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        
        api.createComplaint(complaint, category, "1", body).enqueue(object: Callback<WrappedResponse<Complaint>>{
            override fun onResponse(call: Call<WrappedResponse<Complaint>>, response: Response<WrappedResponse<Complaint>>) {
                if (response.isSuccessful){
                    val body = response.body() as WrappedResponse<Complaint>
                    if (body.status.equals("201")){
                        state.value = ComplaintState.IsSuccess(0)
                    }else {
                        state.value = ComplaintState.IsError("Gagal membuat pengaduan")
                    }
                }else {
                    state.value = ComplaintState.IsError("Telah terjadi kesalahan")
                }
                state.value = ComplaintState.IsLoading(false)
            }
    
            override fun onFailure(call: Call<WrappedResponse<Complaint>>, t: Throwable) {
                state.value = ComplaintState.IsError(t.message)
            }
    
        })
    }
    
    fun validate(complaint: String?) : Boolean{
        state.value =  ComplaintState.Reset
        if (complaint != null){
            if (complaint.isEmpty()){
                state.value = ComplaintState.ShowToast("Pengaduan tidak boleh kosong")
                return false
            }
        }
        return true
    }
    
    fun getComplaints() = complaints
    fun getComplaint() = complaint
    fun getState() = state
}

sealed class ComplaintState {
    data class ShowToast(var message: String) : ComplaintState()
    data class IsLoading(var state: Boolean) : ComplaintState()
    data class Validate(
        var complaint: String? = null,
        var category: String? = null
    ) : ComplaintState()
    data class IsError(var err: String?) : ComplaintState()
    data class IsSuccess(var what: Int? = null) : ComplaintState()
    object NotAuthenticated : ComplaintState()
    object Reset : ComplaintState()
}