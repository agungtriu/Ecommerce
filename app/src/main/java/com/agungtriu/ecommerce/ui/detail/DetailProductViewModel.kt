package com.agungtriu.ecommerce.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val productId: String = savedStateHandle[DetailProductFragment.PRODUCT_ID_KEY] ?: ""

    private val _resultDetail = MutableLiveData<ViewState<DataDetailProduct>?>(null)
    val resultDetail: LiveData<ViewState<DataDetailProduct>?> get() = _resultDetail

    init {
        getDetailProduct()
    }

    fun getDetailProduct() {
        viewModelScope.launch {
            mainRepository.getDetailProduct(productId).collect {
                _resultDetail.value = it
            }
        }
    }

    fun getWishlist(): LiveData<WishlistEntity> =
        mainRepository.getWishlistById(productId).asLiveData()


    fun insertWishlist(wishlistEntity: WishlistEntity) {
        viewModelScope.launch {
            mainRepository.insertWishlist(wishlistEntity)
        }
    }

    fun deleteWishlistById(id: String) {
        viewModelScope.launch {
            mainRepository.deleteWishlist(id)
        }
    }

    fun addCart(cartEntity: CartEntity): LiveData<ViewState<String>> =
        mainRepository.insertCart(cartEntity).asLiveData()
}