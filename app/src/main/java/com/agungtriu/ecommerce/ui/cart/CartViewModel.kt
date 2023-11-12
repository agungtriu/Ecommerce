package com.agungtriu.ecommerce.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _resultCarts = MutableLiveData<List<CartEntity>>()
    val resultCarts: LiveData<List<CartEntity>> get() = _resultCarts

    fun getCarts() {
        viewModelScope.launch {
            mainRepository.getAllCart().collect {
                _resultCarts.value = it
            }
        }
    }

    private var updateJob: Job? = null
    fun updateCart(cartEntity: CartEntity) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(1000)
            mainRepository.updateCart(cartEntity)
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            mainRepository.deleteAllCartSelected()
        }
    }

    fun deleteCart(cartEntity: CartEntity) {
        viewModelScope.launch {
            mainRepository.deleteCart(cartEntity)
        }
    }


    fun checkCartIsSelected(isSelected: Boolean): LiveData<List<CartEntity>> =
        mainRepository.checkCartIsSelected(isSelected).asLiveData()

    fun updateAllCartIsSelected(isSelected: Boolean) {
        viewModelScope.launch {
            mainRepository.updateAllCartSelected(isSelected)
        }
    }

    fun getTotalPay(): LiveData<Int?> = mainRepository.getTotalPay().asLiveData()
}