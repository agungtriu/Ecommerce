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
class CartDaoTest {
    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase
    private lateinit var cartDao: CartDao

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        cartDao = appDatabase.cartDao()
    }

    @After
    fun close() {
        appDatabase.close()
    }

    @Test
    fun insertAndSelectCarts() = runTest {
        cartDao.insertCart(
            cartEntity = DataDummy.dummyCartEntity
        )
        val actual = cartDao.selectCarts().first()
        assertEquals(listOf(DataDummy.dummyCartEntity), actual)
    }

    @Test
    fun insertSelectCountCart() = runTest {
        cartDao.insertCart(
            cartEntity = DataDummy.dummyCartEntity
        )
        val actual = cartDao.selectCountCart().first()
        assertEquals(1, actual)
    }

    @Test
    fun insertUpdateAndSelectCartByProductId() = runTest {
        cartDao.insertCart(
            cartEntity = DataDummy.dummyCartEntity
        )
        cartDao.updateCart(
            id = DataDummy.dummyUpdateCartEntity.id,
            quantity = DataDummy.dummyUpdateCartEntity.quantity ?: 1,
            isSelected = DataDummy.dummyUpdateCartEntity.isSelected ?: false
        )
        val actual = cartDao.selectCartById(DataDummy.dummyCartEntity.id).first()
        assertEquals(DataDummy.dummyUpdateCartEntity, actual)
    }

    @Test
    fun insertUpdateCartsSelectedDeleteCartsSelectedAndSelectCarts() = runTest {
        cartDao.insertCart(
            cartEntity = DataDummy.dummyCartEntity
        )
        cartDao.updateCartsSelected(true)
        cartDao.deleteCartsSelected()
        val actual = cartDao.selectCarts().first()
        assertEquals(0, actual?.size)
    }

    @Test
    fun insertDeleteAndSelectCarts() = runTest {
        cartDao.insertCart(DataDummy.dummyCartEntity)
        cartDao.deleteCart(DataDummy.dummyCartEntity)
        val actual = cartDao.selectCarts().first()
        assertEquals(0, actual?.size)
    }
}
