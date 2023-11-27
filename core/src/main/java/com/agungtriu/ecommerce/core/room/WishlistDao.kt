package com.agungtriu.ecommerce.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert
    suspend fun insertWishlist(wishlist: WishlistEntity)

    @Query("SELECT * FROM wishlists")
    fun selectWishlists(): Flow<List<WishlistEntity>?>

    @Query("SELECT * FROM wishlists WHERE id = :id")
    fun selectWishlistById(id: String): Flow<WishlistEntity?>

    @Query("DELETE FROM wishlists WHERE id = :id")
    suspend fun deleteWishlistById(id: String)

}