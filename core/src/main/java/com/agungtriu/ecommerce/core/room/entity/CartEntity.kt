package com.agungtriu.ecommerce.core.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey
    val id: String,
    val productName: String? = null,
    val image: String? = null,
    val productPrice: Int? = null,
    val store: String? = null,
    val stock: Int? = null,
    val variantPrice: Int? = null,
    val variantName: String? = null,
    val quantity: Int? = 1,
    val isSelected: Boolean? = true
)
