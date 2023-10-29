package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseRegister(
    @SerializedName("code")
    var code: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: DataRegister? = null
)

data class DataRegister(

    @SerializedName("userName")
    var userName: String? = null,

    @SerializedName("userImage")
    var userImage: String? = null,

    @SerializedName("accessToken")
    var accessToken: String? = null,

    @SerializedName("refreshToken")
    var refreshToken: String? = null,

    @SerializedName("expiresAt")
    var expiresAt: Long? = null
)