package com.agungtriu.ecommerce.ui.main.store.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.data.StoreRepository
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
class SearchViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var storeRepository: StoreRepository

    @Before
    fun setUp() {
        storeRepository = mock()
        searchViewModel = SearchViewModel(storeRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResult_success() = runTest {
        whenever(storeRepository.postSearch("")).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(listOf("asus"))
            )
        )
        searchViewModel.searchDebounced("")
        val actual = mutableListOf<ViewState<List<String>>>()
        searchViewModel.result.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(listOf("asus"))
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResult_error() = runTest {
        whenever(storeRepository.postSearch("")).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError404Response)
            )
        )
        searchViewModel.searchDebounced("")
        val actual = mutableListOf<ViewState<List<String>>>()
        searchViewModel.result.observeForever {
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