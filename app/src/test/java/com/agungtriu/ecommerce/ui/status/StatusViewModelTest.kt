package com.agungtriu.ecommerce.ui.status

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.agungtriu.ecommerce.data.CheckoutRepository
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
class StatusViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var statusViewModel: StatusViewModel
    private lateinit var checkoutRepository: CheckoutRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        checkoutRepository = mock()
        savedStateHandle = mock()
        statusViewModel = StatusViewModel(checkoutRepository, savedStateHandle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postRating_success() = runTest {
        whenever(
            checkoutRepository.postRating(requestRating = DataDummy.dummyRequestRating)
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyRating.message!!)
            )
        )

        val actual = mutableListOf<ViewState<String>>()
        statusViewModel.postRating(requestRating = DataDummy.dummyRequestRating).observeForever {
            actual.add(it)
        }

        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyRating.message!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postRating_error() = runTest {
        whenever(
            checkoutRepository.postRating(requestRating = DataDummy.dummyRequestRating)
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            )
        )

        val actual = mutableListOf<ViewState<String>>()
        statusViewModel.postRating(requestRating = DataDummy.dummyRequestRating).observeForever {
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
