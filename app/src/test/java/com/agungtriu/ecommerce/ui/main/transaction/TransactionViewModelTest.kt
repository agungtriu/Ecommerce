package com.agungtriu.ecommerce.ui.main.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.data.CheckoutRepository
import com.agungtriu.ecommerce.helper.ViewState
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
class TransactionViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUp() {
        checkoutRepository = mock()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultTransaction_success() = runTest {
        whenever(checkoutRepository.getTransactions()).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyTransactionResponse.data!!)
            )
        )
        transactionViewModel = TransactionViewModel(checkoutRepository)
        val actual = mutableListOf<ViewState<List<DataTransaction>>>()
        transactionViewModel.resultTransaction.observeForever {
            actual.add(it)
        }

        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyTransactionResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultTransaction_error() = runTest {
        whenever(checkoutRepository.getTransactions()).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError404Response)
            )
        )
        transactionViewModel = TransactionViewModel(checkoutRepository)
        val actual = mutableListOf<ViewState<List<DataTransaction>>>()
        transactionViewModel.resultTransaction.observeForever {
            actual.add(it)
        }

        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError404Response)
            ),
            actual
        )
    }
}