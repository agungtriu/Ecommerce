package com.agungtriu.ecommerce.core.remote

import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.request.RequestRefresh
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.ResponseDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.ResponseLogin
import com.agungtriu.ecommerce.core.remote.model.response.ResponsePayment
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProducts
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProfile
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRating
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRefresh
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRegister
import com.agungtriu.ecommerce.core.remote.model.response.ResponseReviewProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseSearch
import com.agungtriu.ecommerce.core.remote.model.response.ResponseTransaction
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @POST("register")
    suspend fun postRegister(
        @Body requestRegister: RequestRegister
    ): ResponseRegister

    @POST("login")
    suspend fun postLogin(
        @Body requestLogin: RequestLogin
    ): ResponseLogin

    @Multipart
    @POST("profile")
    suspend fun postProfile(
        @Part userName: MultipartBody.Part,
        @Part userImage: MultipartBody.Part?
    ): ResponseProfile

    @POST("refresh")
    suspend fun postRefreshToken(
        @Body token: RequestRefresh
    ): ResponseRefresh

    @POST("products")
    suspend fun getProducts(
        @QueryMap requestFilter: Map<String, String>
    ): ResponseProducts

    @POST("search")
    suspend fun postSearch(
        @Query("query") query: String?,
    ): ResponseSearch

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: String
    ): ResponseDetailProduct

    @GET("review/{id}")
    suspend fun getReviewsByProductId(
        @Path("id") productId: String
    ): ResponseReviewProduct

    @GET("payment")
    suspend fun getPayments(): ResponsePayment

    @POST("fulfillment")
    suspend fun postFulfillment(
        @Body requestFulfillment: RequestFulfillment
    ): ResponseFulfillment

    @POST("rating")
    suspend fun postRating(
        @Body requestRating: RequestRating
    ): ResponseRating

    @GET("transaction")
    suspend fun getTransactions(): ResponseTransaction
}
