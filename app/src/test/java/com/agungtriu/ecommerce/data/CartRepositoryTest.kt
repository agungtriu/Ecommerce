package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.CartDao
import com.agungtriu.ecommerce.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class CartRepositoryTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var cartDao: CartDao

    @Before
    fun setUp() {
        appDatabase = mock()
        cartDao = mock()
        cartRepository = CartRepositoryImp(appDatabase)
    }

    @Test
    fun getCarts_success_notNull() = runTest {
        whenever(appDatabase.cartDao()).thenReturn(cartDao)
        whenever(
            appDatabase.cartDao().selectCarts()
        ).thenReturn(flowOf(listOf(DataDummy.dummyCartEntity)))

        val actual = cartRepository.getCarts().first()
        Assert.assertEquals(listOf(DataDummy.dummyCartEntity), actual)
    }

    @Test
    fun getCarts_success_null() = runTest {
        whenever(appDatabase.cartDao()).thenReturn(cartDao)
        whenever(
            appDatabase.cartDao().selectCarts()
        ).thenReturn(flowOf(null))

        val actual = cartRepository.getCarts().first()
        Assert.assertEquals(null, actual)
    }

    @Test
    fun getCartById_success_notNull() = runTest {
        whenever(appDatabase.cartDao()).thenReturn(cartDao)
        whenever(
            appDatabase.cartDao().selectCartById("")
        ).thenReturn(flowOf(DataDummy.dummyCartEntity))

        val actual = cartRepository.getCartById("").first()
        Assert.assertEquals(DataDummy.dummyCartEntity, actual)
    }

    @Test
    fun getCartById_success_null() = runTest {
        whenever(appDatabase.cartDao()).thenReturn(cartDao)
        whenever(
            appDatabase.cartDao().selectCartById("")
        ).thenReturn(flowOf(null))

        val actual = cartRepository.getCartById("").first()
        Assert.assertEquals(null, actual)
    }
}
