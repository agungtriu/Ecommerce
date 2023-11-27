package com.agungtriu.ecommerce.ui.main.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.Extension.collectDataForTest
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
class StoreVideModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var storeVideModel: StoreVideModel
    private lateinit var storeRepository: StoreRepository

    @Before
    fun setUp() {
        storeRepository = mock()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultProducts() = runTest {
        whenever(storeRepository.getProducts(requestProducts = RequestProducts())).thenReturn(
            flowOf(
                PagingData.from(DataDummy.dummyProductsResponse.data?.items!!)
            )
        )
        storeVideModel = StoreVideModel(storeRepository)
        var actual: PagingData<Product> = PagingData.from(listOf())
        storeVideModel.resultProducts.observeForever {
            actual = it
        }
        advanceUntilIdle()
        assertEquals(DataDummy.dummyProductsResponse.data?.items!!, actual.collectDataForTest())
    }
}
