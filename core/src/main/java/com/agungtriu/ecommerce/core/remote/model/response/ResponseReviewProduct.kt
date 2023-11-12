package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseReviewProduct(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<DataReview>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class DataReview(

    @field:SerializedName("userImage")
    val userImage: String? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("userReview")
    val userReview: String? = null,

    @field:SerializedName("userRating")
    val userRating: Float? = null
)
