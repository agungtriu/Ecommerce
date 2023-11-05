package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseSearch(

    @field:SerializedName("code")
    val code: Int? = null,


    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<String>? = null
)
