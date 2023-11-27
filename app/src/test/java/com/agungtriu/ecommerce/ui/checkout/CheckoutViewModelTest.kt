package com.agungtriu.ecommerce.ui.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataFulfillment
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.data.CheckoutRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment.Companion.CHECKOUT_KEY
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CheckoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var checkoutRepository: CheckoutRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var list: List<CartEntity>

    @Before
    fun setUp() {
        checkoutRepository = mock()
        savedStateHandle =
            SavedStateHandle(mapOf(CHECKOUT_KEY to listOf(DataDummy.dummyCartEntity)))
        list = savedStateHandle.get<List<CartEntity>>(CHECKOUT_KEY)!!
        checkoutViewModel = CheckoutViewModel(checkoutRepository, savedStateHandle)
    }

    @Test
    fun getListProduct() {
        savedStateHandle =
            SavedStateHandle(mapOf(CHECKOUT_KEY to listOf(DataDummy.dummyCartEntity)))
        list = savedStateHandle.get<List<CartEntity>>(CHECKOUT_KEY)!!
        checkoutViewModel = CheckoutViewModel(checkoutRepository, savedStateHandle)
        checkoutViewModel.listProduct.observeForever {
            assertEquals(listOf(DataDummy.dummyCartEntity), it)
        }
    }

    @Test
    fun getDataPayment_success_notNull() {
        val expectedPayment = DataDummy.dummyPaymentsResponse.data?.get(0)?.item?.get(0)
        checkoutViewModel.setDataPayment(expectedPayment)
        checkoutViewModel.dataPayment.observeForever {
            assertEquals(expectedPayment, it)
        }
    }

    @Test
    fun getDataPayment_success_null() {
        checkoutViewModel.setDataPayment(null)
        checkoutViewModel.dataPayment.observeForever {
            assertEquals(null, it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postFulfillment_success() = runTest {
        whenever(
            checkoutRepository.postFulfillment(
                requestFulfillment = RequestFulfillment(
                    "",
                    listOf()
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyFulfillmentResponse.data!!)
            )
        )
        val actual = mutableListOf<ViewState<DataFulfillment>>()
        checkoutViewModel.postFulfillment(requestFulfillment = RequestFulfillment("", listOf()))
            .observeForever {
                actual.add(it)
            }

        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyFulfillmentResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postFulfillment_error() = runTest {
        whenever(
            checkoutRepository.postFulfillment(
                requestFulfillment = RequestFulfillment(
                    "",
                    listOf()
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyFulfillmentErrorResponse)
            )
        )
        val actual = mutableListOf<ViewState<DataFulfillment>>()
        checkoutViewModel.postFulfillment(requestFulfillment = RequestFulfillment("", listOf()))
            .observeForever {
                actual.add(it)
            }

        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyFulfillmentErrorResponse)
            ),
            actual
        )
    }
}