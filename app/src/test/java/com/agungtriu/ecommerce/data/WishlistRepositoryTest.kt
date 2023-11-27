package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.WishlistDao
import com.agungtriu.ecommerce.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class WishlistRepositoryTest {
    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var wishlistDao: WishlistDao

    @Before
    fun setUp() {
        appDatabase = mock()
        wishlistDao = mock()
        wishlistRepository = WishlistRepositoryImp(appDatabase)
    }

    @Test
    fun getWishlists_success_notNull() = runTest {
        whenever(appDatabase.wishlistDao()).thenReturn(wishlistDao)
        whenever(
            appDatabase.wishlistDao().selectWishlists()
        ).thenReturn(flowOf(listOf(DataDummy.dummyWishlistEntity)))

        val actual = wishlistRepository.getWishlists().first()
        assertEquals(listOf(DataDummy.dummyWishlistEntity), actual)
    }

    @Test
    fun getWishlists_success_null() = runTest {
        whenever(appDatabase.wishlistDao()).thenReturn(wishlistDao)
        whenever(
            appDatabase.wishlistDao().selectWishlists()
        ).thenReturn(flowOf(null))

        val actual = wishlistRepository.getWishlists().first()
        assertEquals(null, actual)
    }

    @Test
    fun getWishlistById_success_notNull() = runTest {
        whenever(appDatabase.wishlistDao()).thenReturn(wishlistDao)
        whenever(
            appDatabase.wishlistDao().selectWishlistById("")
        ).thenReturn(flowOf(DataDummy.dummyWishlistEntity))

        val actual = wishlistRepository.getWishlistById("").first()
        assertEquals(DataDummy.dummyWishlistEntity, actual)
    }

    @Test
    fun getWishlistById_success_null() = runTest {
        whenever(appDatabase.wishlistDao()).thenReturn(wishlistDao)
        whenever(
            appDatabase.wishlistDao().selectWishlistById("")
        ).thenReturn(flowOf(null))

        val actual = wishlistRepository.getWishlistById("").first()
        assertEquals(null, actual)
    }
}