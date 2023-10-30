package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @field:SerializedName("code") val code: Int? = null,
    @field:SerializedName("message") val message: String? = null
)
