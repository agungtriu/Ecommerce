package com.agungtriu.ecommerce.core.remote.model.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class RequestProfile(
    val userName: MultipartBody.Part,
    val userImage: MultipartBody.Part? = null
)