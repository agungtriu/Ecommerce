package com.agungtriu.ecommerce.ui.main.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private var _resultWishlists = MutableLiveData<List<WishlistEntity>>()
    val resultWishlist: LiveData<List<WishlistEntity>> get() = _resultWishlists

    fun getWishlists() {
        viewModelScope.launch {
            mainRepository.getWishlists().collect {
                _resultWishlists.value = it
            }
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