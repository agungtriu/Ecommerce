package com.agungtriu.ecommerce.core.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.agungtriu.ecommerce.core.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [33])
class WishlistDaoTest {
    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase
    private lateinit var wishlistDao: WishlistDao

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        wishlistDao = appDatabase.wishlistDao()
    }

    @After
    fun close() {
        appDatabase.close()
    }

    @Test
    fun insertAndSelectWishlists() = runTest {
        wishlistDao.insertWishlist(
            wishlist = DataDummy.dummyWishlistEntity
        )
        val actual = wishlistDao.selectWishlists().first()
        assertEquals(listOf(DataDummy.dummyWishlistEntity), actual)
    }

    @Test
    fun insertAndSelectWishlistByProductId() = runTest {
        wishlistDao.insertWishlist(
            wishlist = DataDummy.dummyWishlistEntity
        )
        val actual = wishlistDao.selectWishlistById(DataDummy.dummyWishlistEntity.id).first()
        assertEquals(DataDummy.dummyWishlistEntity, actual)
    }

    @Test
    fun insertDeleteByIdAndSelectWishlistById() = runTest {
        wishlistDao.insertWishlist(
            wishlist = DataDummy.dummyWishlistEntity
        )
        wishlistDao.deleteWishlistById(DataDummy.dummyWishlistEntity.id)
        val actual = wishlistDao.selectWishlistById(DataDummy.dummyWishlistEntity.id).first()
        assertEquals(null, actual)
    }
}
