package com.agungtriu.ecommerce.ui.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StoreVideModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>> =
        mainRepository.getProducts(requestProducts).cachedIn(viewModelScope)
}