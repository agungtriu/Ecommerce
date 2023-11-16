package com.agungtriu.ecommerce.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val mainRepository: MainRepository
) :
    ViewModel() {
    private val _resultPayments = MutableLiveData<ViewState<List<DataTypePayment>>>()
    val resultPayment: LiveData<ViewState<List<DataTypePayment>>> get() = _resultPayments

    fun getPayment() {
        viewModelScope.launch {
            mainRepository.getPayments().collect {
                _resultPayments.value = it
            }
        }
    }

}