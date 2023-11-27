package com.agungtriu.ecommerce.ui.review

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.review.ReviewFragment.Companion.REVIEW_KEY
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
class ReviewViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var storeRepository: StoreRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        storeRepository = mock()
        savedStateHandle = SavedStateHandle(mapOf(REVIEW_KEY to "asf"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultReview_success() = runTest {
        val id = savedStateHandle.get<String>(REVIEW_KEY)
        whenever(storeRepository.getReviewsByProductId(id!!)).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(
                    DataDummy.dummyReviewByProductId.data!!
                )
            )
        )
        reviewViewModel = ReviewViewModel(storeRepository, savedStateHandle)

        val actual = mutableListOf<ViewState<List<DataReview>>>()
        reviewViewModel.resultReview.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(
                    DataDummy.dummyReviewByProductId.data!!
                )
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultReview_error() = runTest {
        val id = savedStateHandle.get<String>(REVIEW_KEY)
        whenever(storeRepository.getReviewsByProductId(id!!)).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(
                    DataDummy.dummyError404Response
                )
            )
        )
        reviewViewModel = ReviewViewModel(storeRepository, savedStateHandle)

        val actual = mutableListOf<ViewState<List<DataReview>>>()
        reviewViewModel.resultReview.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(
                    DataDummy.dummyError404Response
                )
            ),
            actual
        )
    }
}