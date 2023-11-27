package com.agungtriu.ecommerce.core.remote.model.request

data class RequestFulfillment(
    val payment: String,
    val items: List<ProductFulfillment>
)

data class ProductFulfillment(
    val productId: String,
    val variantName: String,
    val quantity: Int
)