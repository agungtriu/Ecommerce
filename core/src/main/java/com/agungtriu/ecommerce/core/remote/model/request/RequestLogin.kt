package com.agungtriu.ecommerce.core.remote.model.request

data class RequestLogin(
    val email: String,
    val password: String,
    val firebaseToken: String
)
