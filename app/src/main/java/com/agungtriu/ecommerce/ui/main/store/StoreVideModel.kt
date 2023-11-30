package com.agungtriu.ecommerce.ui.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.ui.main.store.filter.FilterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreVideModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    private var _resultProducts = MutableLiveData<PagingData<Product>>()
    val resultProducts: LiveData<PagingData<Product>> get() = _resultProducts
    var requestProducts = RequestProducts()
    var isGrid = false
    var error = ResponseError()

    init {
        getProducts()
    }

    fun getProducts(requestProducts: RequestProducts = RequestProducts()) {
        viewModelScope.launch {
            storeRepository.getProducts(requestProducts).cachedIn(viewModelScope).collect {
                _resultProducts.value = it
            }
        }
    }

    fun getLangTheme(): LiveData<ThemeLangModel> = mainRepository.getThemeLang().asLiveData()
}
