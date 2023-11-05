package com.agungtriu.ecommerce.core.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseProducts(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: DataProducts? = null,

    )

data class DataProducts(

    @field:SerializedName("pageIndex")
    val pageIndex: Int? = null,

    @field:SerializedName("itemsPerPage")
    val itemsPerPage: Int? = null,

    @field:SerializedName("currentItemCount")
    val currentItemCount: Int? = null,

    @field:SerializedName("totalPages")
    val totalPages: Int? = null,

    @field:SerializedName("items")
    val items: List<Product>? = null
)

data class Product(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("sale")
    val sale: Int? = null,

    @field:SerializedName("productId")
    val productId: String? = null,

    @field:SerializedName("store")
    val store: String? = null,

    @field:SerializedName("productRating")
    val productRating: Any? = null,

    @field:SerializedName("brand")
    val brand: String? = null,

    @field:SerializedName("productName")
    val productName: String? = null,

    @field:SerializedName("productPrice")
    val productPrice: Int? = null
)
