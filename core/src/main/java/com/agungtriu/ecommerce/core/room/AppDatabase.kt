package com.agungtriu.ecommerce.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity

@Database(
    entities = [WishlistEntity::class, CartEntity::class, NotificationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun cartDao(): CartDao
    abstract fun notificationDao(): NotificationDao
}
