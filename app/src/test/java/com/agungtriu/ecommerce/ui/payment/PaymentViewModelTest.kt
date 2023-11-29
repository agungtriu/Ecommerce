package com.agungtriu.ecommerce.ui.payment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.data.CheckoutRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class PaymentViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUp() {
        checkoutRepository = mock()
        paymentViewModel = PaymentViewModel(checkoutRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFirebasePayment_success() = runTest {
        whenever(checkoutRepository.getFirebasePayments()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Success(DataDummy.dummyPaymentsResponse.data!!))
            }
        )

        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        paymentViewModel.getFirebasePayment().observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyPaymentsResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFirebasePayment_error() = runTest {
        whenever(checkoutRepository.getFirebasePayments()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Error(DataDummy.dummyError404Response))
            }
        )

        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        paymentViewModel.getFirebasePayment().observeForever {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateFirebasePayment_success() = runTest {
        whenever(checkoutRepository.updateFirebasePayments()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Success(DataDummy.dummyPaymentsResponse.data!!))
            }
        )

        paymentViewModel.updateFirebasePayment().observeForever {
            if (it is ViewState.Success) {
                Assert.assertEquals(DataDummy.dummyPaymentsResponse.data, it.data)
            }
        }

        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        paymentViewModel.updateFirebasePayment().observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyPaymentsResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateFirebasePayment_error() = runTest {
        whenever(checkoutRepository.updateFirebasePayments()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Error(DataDummy.dummyError404Response))
            }
        )

        paymentViewModel.updateFirebasePayment().observeForever {
            if (it is ViewState.Success) {
                Assert.assertEquals(DataDummy.dummyPaymentsResponse.data, it.data)
            }
        }

        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        paymentViewModel.updateFirebasePayment().observeForever {
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
