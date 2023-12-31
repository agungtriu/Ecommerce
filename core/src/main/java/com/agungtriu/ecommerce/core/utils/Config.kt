package com.agungtriu.ecommerce.core.utils

object Config {
    const val DATASTORE_NAME = "ecommerce"

//    const val API_BASE_URL = "http://192.168.101.126:5000/"
    const val API_BASE_URL = "http://192.168.43.224:5000/"
//    const val API_BASE_URL = "http://192.168.1.9:5000/"
//    const val API_BASE_URL = "http://172.17.20.248:5000/"

    const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"

    val PRELOGIN_ENDPOINT = listOf(
        "${API_BASE_URL}login",
        "${API_BASE_URL}register",
        "${API_BASE_URL}refresh"
    )

    const val DATABASE_NAME = "ecommerce"
}
