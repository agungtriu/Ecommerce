package com.agungtriu.ecommerce.core.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "carts")
@Parcelize
data class CartEntity(
    @PrimaryKey
    val id: String,
    val productName: String? = null,
    val image: String? = null,
    val productPrice: Int? = null,
    val brand: String? = null,
    val store: String? = null,
    val stock: Int? = null,
    val variantPrice: Int? = null,
    val variantName: String? = null,
    val quantity: Int? = 1,
    val isSelected: Boolean? = true
) : Parcelable
