package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun insertWishlist(wishlistEntity: WishlistEntity)
    suspend fun deleteWishlist(id: String)
    fun getWishlists(): Flow<List<WishlistEntity>?>
    fun getWishlistById(id: String): Flow<WishlistEntity?>

}