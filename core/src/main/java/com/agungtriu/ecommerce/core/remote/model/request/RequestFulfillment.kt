package com.agungtriu.ecommerce.core.remote.model.request

data class RequestFulfillment(
    val payment: String,
    val items: List<Product>
)

data class Product(
    val productId: String,
    val variantName: String,
    val quantity: Int
)