package com.agungtriu.ecommerce.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.data.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {
    private val _resultCarts = MutableLiveData<List<CartEntity>>()
    val resultCarts: LiveData<List<CartEntity>> get() = _resultCarts

    fun getCarts() {
        viewModelScope.launch {
            cartRepository.getCarts().collect {
                _resultCarts.value = it
            }
        }
    }

    fun getProductById(id: String) = storeRepository.getProductById(id).asLiveData()

    fun updateCart(cartEntity: CartEntity) {
        viewModelScope.launch {
            cartRepository.updateCart(cartEntity)
        }
    }

    fun deleteCartsSelected() {
        viewModelScope.launch {
            cartRepository.deleteCartsSelected()
        }
    }

    fun deleteCart(cartEntity: CartEntity) {
        viewModelScope.launch {
            cartRepository.deleteCart(cartEntity)
        }
    }

    fun updateCartsIsSelected(isSelected: Boolean) {
        viewModelScope.launch {
            cartRepository.updateCartsSelected(isSelected)
        }
    }
}
