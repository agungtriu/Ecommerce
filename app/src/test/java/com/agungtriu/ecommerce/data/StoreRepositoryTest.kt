package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.Extension.toHttpException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class StoreRepositoryTest {

    private lateinit var storeRepository: StoreRepository
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = Mockito.mock()
        storeRepository = StoreRepositoryImp(apiService)
    }

    @Test
    fun getProducts() = runTest {
        whenever(apiService.getProducts()).thenReturn(DataDummy.dummyProductsResponse)
        val actual = storeRepository.getProducts(requestProducts = RequestProducts()).first()
        assertNotNull(actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductById_success() = runTest {
        whenever(apiService.getProductById(DataDummy.dummyProductByIdResponse.data?.productId!!)).thenReturn(
            DataDummy.dummyProductByIdResponse
        )

        val actual = mutableListOf<ViewState<DataDetailProduct>>()
        storeRepository.getProductById(DataDummy.dummyProductByIdResponse.data?.productId!!)
            .collect {
                actual.add(it)
            }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProductByIdResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductById_error() = runTest {
        whenever(apiService.getProductById(DataDummy.dummyProductByIdResponse.data?.productId!!)).thenThrow(
            DataDummy.dummyError404Response.toHttpException()
        )

        val actual = mutableListOf<ViewState<DataDetailProduct>>()
        storeRepository.getProductById(DataDummy.dummyProductByIdResponse.data?.productId!!)
            .collect {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getReviewsByProductId_success() = runTest {
        whenever(apiService.getReviewsByProductId("")).thenReturn(DataDummy.dummyReviewByProductId)
        val actual = mutableListOf<ViewState<List<DataReview>>>()
        storeRepository.getReviewsByProductId("").collect {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyReviewByProductId.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getReviewsByProductId_error() = runTest {
        whenever(apiService.getReviewsByProductId("")).thenThrow(DataDummy.dummyError404Response.toHttpException())
        val actual = mutableListOf<ViewState<List<DataReview>>>()
        storeRepository.getReviewsByProductId("").collect {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postSearch_success() = runTest {
        whenever(apiService.postSearch("")).thenReturn(DataDummy.dummySearchResponse)

        val actual = mutableListOf<ViewState<List<String>>>()
        storeRepository.postSearch("").collect {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummySearchResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postSearch_error() = runTest {
        whenever(apiService.postSearch("")).thenThrow(DataDummy.dummyError404Response.toHttpException())

        val actual = mutableListOf<ViewState<List<String>>>()
        storeRepository.postSearch("").collect {
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