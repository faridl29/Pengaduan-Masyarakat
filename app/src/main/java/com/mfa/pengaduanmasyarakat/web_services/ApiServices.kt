package com.mfa.pengaduanmasyarakat.web_services

import com.mfa.pengaduanmasyarakat.models.Complaint
import com.mfa.pengaduanmasyarakat.models.User
import com.mfa.pengaduanmasyarakat.utils.WrappedListResponse
import com.mfa.pengaduanmasyarakat.utils.WrappedResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    @Multipart
    @POST("users/login")
    fun login(
        @Part("nik") email: String,
        @Part("password") password: String)
              : Call<WrappedResponse<User>>
    
    @GET("complaints")
    fun allComplaint(
        @Header("Authorization") token: String)
        : Call<WrappedListResponse<Complaint>>
    
    @Multipart
    @POST("complaints/create")
    fun createComplaint(
        @Part("complaint") title: String?,
        @Part("category") content: String?,
        @Part("user_id") user_id: String?,
        @Part file: MultipartBody.Part?)
        : Call<WrappedResponse<Complaint>>
}