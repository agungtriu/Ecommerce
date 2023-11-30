package com.agungtriu.ecommerce.core.remote.model.request

data class RequestRegister(
    val email: String,
    val password: String,
    val firebaseToken: String
)
