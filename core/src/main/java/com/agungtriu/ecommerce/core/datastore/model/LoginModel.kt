package com.agungtriu.ecommerce.core.datastore.model

data class LoginModel(
    val isLogin: Boolean,
    val userName: String? = "",
    val userImage: String? = "",
    val accessToken: String? = "",
    val refreshToken: String? = "",
    val isAuthorized: Boolean
)
