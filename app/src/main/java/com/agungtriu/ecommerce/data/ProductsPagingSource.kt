package com.agungtriu.ecommerce.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.Product

class ProductsPagingSource(
    private val apiService: ApiService,
    private val requestProducts: RequestProducts
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val position = params.key ?: 1
            val requestFilter = mutableMapOf<String, String>()
            requestFilter["page"] = position.toString()
            requestFilter["limit"] = params.loadSize.toString()
            if (!requestProducts.search.isNullOrEmpty()) {
                requestFilter["search"] = requestProducts.search!!
            }
            if (!requestProducts.brand.isNullOrEmpty()) {
                requestFilter["brand"] = requestProducts.brand!!
            }
            if (requestProducts.highest != null) {
                requestFilter["highest"] = requestProducts.highest.toString()
            }
            if (requestProducts.lowest != null) {
                requestFilter["lowest"] = requestProducts.lowest.toString()
            }
            if (!requestProducts.sort.isNullOrEmpty()) {
                requestFilter["sort"] = requestProducts.sort!!
            }

            val response = apiService.getProducts(requestFilter = requestFilter)
            val nextKey =
                if (position == response.data?.totalPages || response.data?.items.isNullOrEmpty()) {
                    null
                } else {
                    position + 1
                }
            LoadResult.Page(
                data = response.data?.items ?: emptyList(),
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
