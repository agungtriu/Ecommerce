package com.agungtriu.ecommerce.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepositoryImp @Inject constructor(
    private val apiService: ApiService
) : StoreRepository {

    override fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1),
        pagingSourceFactory = {
            ProductsPagingSource(apiService, requestProducts)
        },
    ).flow

    override fun getProductById(productId: String): Flow<ViewState<DataDetailProduct>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.getProductById(productId = productId)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data Product not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override fun getReviewsByProductId(productId: String): Flow<ViewState<List<DataReview>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.getReviewsByProductId(productId)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data review not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override suspend fun postSearch(query: String): Flow<ViewState<List<String>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.postSearch(query = query)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data search not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }
}