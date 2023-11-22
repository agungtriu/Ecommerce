package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRepositoryImp @Inject constructor(private val appDatabase: AppDatabase) :
    WishlistRepository {
    override suspend fun insertWishlist(wishlistEntity: WishlistEntity) {
        appDatabase.wishlistDao().insertWishlist(wishlistEntity)
    }

    override suspend fun deleteWishlist(id: String) {
        appDatabase.wishlistDao().deleteWishlistById(id)
    }

    override fun getWishlists(): Flow<List<WishlistEntity>> =
        appDatabase.wishlistDao().selectWishlists()

    override fun getWishlistById(id: String): Flow<WishlistEntity> =
        appDatabase.wishlistDao().selectWishlistById(id)
}