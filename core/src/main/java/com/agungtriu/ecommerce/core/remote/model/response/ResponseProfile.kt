package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("code")
    var code: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: DataProfile? = null
)

data class DataProfile(

    @SerializedName("userName")
    var userName: String? = null,

    @SerializedName("userImage")
    var userImage: String? = null
)
