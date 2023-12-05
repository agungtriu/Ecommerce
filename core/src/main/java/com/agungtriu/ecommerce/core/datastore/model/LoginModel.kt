package com.agungtriu.ecommerce.core.datastore.model

data class LoginModel(
    val isLogin: Boolean? = false,
    val userName: String? = "",
    val userImage: String? = "",
    val accessToken: String? = "",
    val refreshToken: String? = "",
    val isAuthorized: Boolean? = false
)
