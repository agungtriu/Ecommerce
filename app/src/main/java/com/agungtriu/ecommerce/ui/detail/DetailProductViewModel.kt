package com.agungtriu.ecommerce.ui.detail

import androidx.lifecycle.LiveData
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

    init {
        getDetailProduct()
        getWishlist()
    }

    fun getDetailProduct(): LiveData<ViewState<DataDetailProduct>> =
        mainRepository.getDetailProduct(productId).asLiveData()

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