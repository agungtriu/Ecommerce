package com.agungtriu.ecommerce.core.remote.model.request

import okhttp3.MultipartBody

data class RequestProfile(
    val userName: MultipartBody.Part,
    val userImage: MultipartBody.Part? = null
)