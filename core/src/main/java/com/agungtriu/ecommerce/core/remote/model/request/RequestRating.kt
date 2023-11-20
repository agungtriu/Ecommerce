package com.agungtriu.ecommerce.core.remote.model.request

data class RequestRating(
    val invoiceId: String,
    val rating: Int? = null,
    val review: String? = null
)