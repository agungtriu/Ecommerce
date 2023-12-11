package com.agungtriu.ecommerce.core.utils

import com.agungtriu.ecommerce.core.remote.model.response.ResponseDetailProduct
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity

object Extension {
    fun ResponseDetailProduct.toCart(quantity: Int): CartEntity {
        return CartEntity(
            id = this.data?.productId ?: "",
            productName = this.data?.productName,
            image = this.data?.image?.get(0),
            productPrice = this.data?.productPrice,
            brand = this.data?.brand,
            store = this.data?.store,
            stock = this.data?.stock,
            variantPrice = this.data?.productVariant?.get(0)?.variantPrice?.plus(
                this.data?.productPrice ?: 0
            ),
            variantName = this.data?.productName,
            quantity = quantity
        )
    }

    fun ResponseDetailProduct.toWishlist(): WishlistEntity {
        return WishlistEntity(
            id = this.data?.productId ?: "",
            productName = this.data?.productName,
            image = this.data?.image?.get(0),
            productPrice = this.data?.productPrice,
            brand = this.data?.brand,
            store = this.data?.store,
            productRating = this.data?.productRating,
            sale = this.data?.sale,
            stock = this.data?.stock,
            variantPrice = this.data?.productVariant?.get(0)?.variantPrice?.plus(
                this.data?.productPrice ?: 0
            ),
            variantName = this.data?.productName,
        )
    }

    fun ResponseNotification.toNotifications(isRead: Boolean = false): NotificationEntity {
        return NotificationEntity(
            id = 1,
            title = this.message?.data?.title,
            body = this.message?.data?.body,
            image = this.message?.data?.image,
            date = this.message?.data?.date,
            time = this.message?.data?.time,
            type = this.message?.data?.type,
            isRead = isRead
        )
    }
}
