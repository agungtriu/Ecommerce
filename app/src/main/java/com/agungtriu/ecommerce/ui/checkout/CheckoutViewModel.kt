package com.agungtriu.ecommerce.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataPayment
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.data.CheckoutRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.checkout.CheckoutFragment.Companion.CHECKOUT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    val list = savedStateHandle[CHECKOUT_KEY] ?: listOf<CartEntity>()
    private val _listProduct = MutableLiveData<List<CartEntity>>()
    val listProduct: LiveData<List<CartEntity>> get() = _listProduct

    init {
        _listProduct.value = list
    }

    private val _dataPayment = MutableLiveData<DataPayment?>(null)
    val dataPayment: LiveData<DataPayment?> get() = _dataPayment

    fun setDataPayment(dataPayment: DataPayment?) {
        _dataPayment.value = dataPayment
    }

    fun updateCheckout(cartEntity: CartEntity, position: Int) {
        val list = listProduct.value!!.toMutableList()
        list[position] = cartEntity
        _listProduct.value = list
    }

    fun postFulfillment(requestFulfillment: RequestFulfillment): LiveData<ViewState<DataFulfillment>> {
        return runBlocking {
            checkoutRepository.postFulfillment(requestFulfillment).asLiveData()
        }
    }
}