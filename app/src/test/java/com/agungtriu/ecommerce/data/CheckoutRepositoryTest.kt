package com.agungtriu.ecommerce.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.response.DataFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.data.firebase.RemoteConfig
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.Extension.toHttpException
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class CheckoutRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var checkoutRepository: CheckoutRepository
    private lateinit var apiService: ApiService
    private lateinit var remoteConfig: RemoteConfig

    @Before
    fun setUp() {
        apiService = mock()
        remoteConfig = mock()
        checkoutRepository = CheckoutRepositoryImp(apiService, remoteConfig)
    }

    @Test
    fun getPayments_success() = runTest {
        whenever(apiService.getPayments()).thenReturn(DataDummy.dummyPaymentsResponse)
        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        checkoutRepository.getPayments().collect {
            actual.add(it)
        }

        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyPaymentsResponse.data)
            ),
            actual
        )
    }

    @Test
    fun getPayments_error() = runTest {
        whenever(apiService.getPayments()).thenThrow(DataDummy.dummyError404Response.toHttpException())
        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        checkoutRepository.getPayments().collect {
            actual.add(it)
        }

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
    fun getFirebasePayments_success() = runTest {
        whenever(remoteConfig.getPayment()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Success(DataDummy.dummyPaymentsResponse.data!!))
            }
        )

        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        checkoutRepository.getFirebasePayments().observeForever {
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
    fun getFirebasePayments_error() = runTest {
        whenever(remoteConfig.getPayment()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Error(DataDummy.dummyError404Response))
            }
        )

        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        checkoutRepository.getFirebasePayments().observeForever {
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
    fun updateFirebasePayments_success() = runTest {
        whenever(remoteConfig.updatePayment()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Success(DataDummy.dummyPaymentsResponse.data!!))
            }
        )
        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        checkoutRepository.updateFirebasePayments().observeForever {
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
    fun updateFirebasePayments_error() = runTest {
        whenever(remoteConfig.updatePayment()).thenReturn(
            liveData {
                this.emit(ViewState.Loading)
                this.emit(ViewState.Error(DataDummy.dummyError404Response))
            }
        )
        val actual = mutableListOf<ViewState<List<DataTypePayment>>>()
        checkoutRepository.updateFirebasePayments().observeForever {
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
    fun postFulfillment_success() = runTest {
        whenever(
            apiService.postFulfillment(
                requestFulfillment = RequestFulfillment(
                    payment = "",
                    items = listOf()
                )
            )
        ).thenReturn(DataDummy.dummyFulfillmentResponse)

        val actual = mutableListOf<ViewState<DataFulfillment>>()
        checkoutRepository.postFulfillment(
            requestFulfillment = RequestFulfillment(
                payment = "",
                items = listOf()
            )
        ).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyFulfillmentResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postFulfillment_error() = runTest {
        whenever(
            apiService.postFulfillment(
                requestFulfillment = RequestFulfillment(
                    payment = "",
                    items = listOf()
                )
            )
        ).thenThrow(DataDummy.dummyError401Response.toHttpException())

        val actual = mutableListOf<ViewState<DataFulfillment>>()
        checkoutRepository.postFulfillment(
            requestFulfillment = RequestFulfillment(
                payment = "",
                items = listOf()
            )
        ).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postRating_success() = runTest {
        whenever(
            apiService.postRating(
                requestRating = RequestRating(
                    invoiceId = "1",
                    rating = 5,
                    review = null
                )
            )
        ).thenReturn(DataDummy.dummyRating)

        val actual = mutableListOf<ViewState<String>>()
        checkoutRepository.postRating(
            requestRating = RequestRating(
                invoiceId = "1",
                rating = 5,
                review = null
            )
        ).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyRating.message)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postRating_error() = runTest {
        whenever(
            apiService.postRating(
                requestRating = RequestRating(
                    invoiceId = "1",
                    rating = 5,
                    review = null
                )
            )
        ).thenThrow(DataDummy.dummyError401Response.toHttpException())

        val actual = mutableListOf<ViewState<String>>()
        checkoutRepository.postRating(
            requestRating = RequestRating(
                invoiceId = "1",
                rating = 5,
                review = null
            )
        ).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTransactions_success() = runTest {
        whenever(apiService.getTransactions()).thenReturn(DataDummy.dummyTransactionResponse)
        val actual = mutableListOf<ViewState<List<DataTransaction>>>()
        checkoutRepository.getTransactions().collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyTransactionResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTransactions_error() = runTest {
        whenever(apiService.getTransactions()).thenThrow(DataDummy.dummyError401Response.toHttpException())
        val actual = mutableListOf<ViewState<List<DataTransaction>>>()
        checkoutRepository.getTransactions().collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            ),
            actual
        )
    }
}
