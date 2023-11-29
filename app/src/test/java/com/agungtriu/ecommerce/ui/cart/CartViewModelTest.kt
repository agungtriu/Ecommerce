package com.agungtriu.ecommerce.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        cartRepository = mock()
        cartViewModel = CartViewModel(cartRepository)
    }

    @Test
    fun getResultCarts_success_notNull() = runTest {
        whenever(cartRepository.getCarts()).thenReturn(flowOf(listOf(DataDummy.dummyCartEntity)))
        cartViewModel.getCarts()
        cartViewModel.resultCarts.observeForever {
            Assert.assertEquals(listOf(DataDummy.dummyCartEntity), it)
        }
    }

    @Test
    fun getResultCarts_success_null() = runTest {
        whenever(cartRepository.getCarts()).thenReturn(flowOf(listOf()))
        cartViewModel.getCarts()
        cartViewModel.resultCarts.observeForever {
            Assert.assertEquals(0, it.size)
        }
    }
}
