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
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.data.WishlistRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val productId: String = savedStateHandle[DetailProductFragment.PRODUCT_ID_KEY] ?: ""
    var stateDetail: String = savedStateHandle[DetailProductFragment.FROM_KEY] ?: ""

    var selectedVariantName = ""
    var selectedVariantPrice = 0
    var sliderPosition: Int? = 0

    private val _resultDetail = MutableLiveData<ViewState<DataDetailProduct>>()
    val resultDetail: LiveData<ViewState<DataDetailProduct>> get() = _resultDetail

    init {
        getProductById()
    }

    fun getProductById() {
        viewModelScope.launch {
            storeRepository.getProductById(productId).collect {
                _resultDetail.value = it
            }
        }
    }

    fun getWishlistByProductId(): LiveData<WishlistEntity?> =
        wishlistRepository.getWishlistById(productId).asLiveData()

    fun insertWishlist(wishlistEntity: WishlistEntity) {
        viewModelScope.launch {
            wishlistRepository.insertWishlist(wishlistEntity)
        }
    }

    fun deleteWishlistById(id: String) {
        viewModelScope.launch {
            wishlistRepository.deleteWishlist(id)
        }
    }

    fun addCart(cartEntity: CartEntity): LiveData<ViewState<String>> =
        cartRepository.insertCart(cartEntity).asLiveData()
}
