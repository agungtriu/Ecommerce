package com.agungtriu.ecommerce.core.remote

import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.ResponseLogin
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProfile
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRefresh
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("register")
    suspend fun doRegister(
        @Body requestRegister: RequestRegister
    ): ResponseRegister

    @POST("login")
    suspend fun doLogin(
        @Body requestLogin: RequestLogin
    ): ResponseLogin

    @Multipart
    @POST("profile")
    suspend fun registerProfile(
        @Part("userName") userName: RequestBody,
        @Part userImage: MultipartBody.Part?
    ): ResponseProfile

    @POST("refresh")
    suspend fun doRefreshToken(
        @Field("token") token: String
    ): ResponseRefresh

}