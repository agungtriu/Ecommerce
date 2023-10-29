package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseRefresh(
    @SerializedName("code")
    var code: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: DataRefresh? = null
)

data class DataRefresh(

    @SerializedName("accessToken")
    var accessToken: String? = null,

    @SerializedName("refreshToken")
    var refreshToken: String? = null,

    @SerializedName("expiresAt")
    var expiresAt: Long? = null
)
