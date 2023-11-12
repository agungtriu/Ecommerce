package com.agungtriu.ecommerce.core.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlists")
data class WishlistEntity(
    @PrimaryKey
    val id: String,
    val productName: String? = null,
    val image: String? = null,
    val productPrice: Int? = null,
    val store: String? = null,
    val productRating: Float? = null,
    val sale: Int? = null,
    val stock: Int? = null,
    val variantPrice: Int? = null,
    val variantName: String? = null,
)