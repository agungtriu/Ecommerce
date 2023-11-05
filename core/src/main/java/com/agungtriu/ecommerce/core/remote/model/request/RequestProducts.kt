package com.agungtriu.ecommerce.core.remote.model.request

data class RequestProducts(
    var search: String? = null,
    val brand: String? = null,
    val lowest: Int? = null,
    val highest: Int? = null,
    val sort: String? = null,
    val limit: Int? = null,
    val page: Int? = null
)