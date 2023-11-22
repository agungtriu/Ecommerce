package com.agungtriu.ecommerce.ui.main.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.data.WishlistRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository
) :
    ViewModel() {
    private var _resultWishlists = MutableLiveData<List<WishlistEntity>>()
    val resultWishlist: LiveData<List<WishlistEntity>> get() = _resultWishlists

    var isGrid = false

    fun getWishlists() {
        viewModelScope.launch {
            wishlistRepository.getWishlists().collect {
                _resultWishlists.value = it
            }
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