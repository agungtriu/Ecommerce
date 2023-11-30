package com.agungtriu.ecommerce.data

import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>>
    fun getProductById(productId: String): Flow<ViewState<DataDetailProduct>>
    fun getReviewsByProductId(productId: String): Flow<ViewState<List<DataReview>>>
    suspend fun postSearch(query: String): Flow<ViewState<List<String>>>
}
