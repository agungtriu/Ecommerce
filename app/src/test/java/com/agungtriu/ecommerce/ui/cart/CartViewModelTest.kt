package com.agungtriu.ecommerce.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
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
    private lateinit var storeRepository: StoreRepository

    @Before
    fun setUp() {
        cartRepository = mock()
        storeRepository = mock()
        cartViewModel = CartViewModel(cartRepository, storeRepository)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductById_success() = runTest {
        whenever(storeRepository.getProductById("")).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProductByIdResponse.data!!)
            )
        )
        val actual = mutableListOf<ViewState<DataDetailProduct>>()
        cartViewModel.getProductById("").observeForever {
            actual.add(it)
        }

        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProductByIdResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductById_error() = runTest {
        whenever(storeRepository.getProductById("")).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError404Response)
            )
        )
        val actual = mutableListOf<ViewState<DataDetailProduct>>()
        cartViewModel.getProductById("").observeForever {
            actual.add(it)
        }

        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError404Response)
            ),
            actual
        )
    }
}
