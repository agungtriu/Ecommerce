package com.agungtriu.ecommerce.ui.main.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.data.CheckoutRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    ViewModel() {

    private val _resultTransaction = MutableLiveData<ViewState<List<DataTransaction>>>()
    val resultTransaction: LiveData<ViewState<List<DataTransaction>>> get() = _resultTransaction

    init {
        getTransactions()
    }

    fun getTransactions() {
        viewModelScope.launch {
            checkoutRepository.getTransactions().collect {
                _resultTransaction.value = it
            }
        }
    }
}