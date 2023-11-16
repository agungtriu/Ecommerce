package com.agungtriu.ecommerce.ui.main.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val _resultTransaction = MutableLiveData<ViewState<List<DataTransaction>>>()
    val resultTransaction: LiveData<ViewState<List<DataTransaction>>> get() = _resultTransaction

    init {
        getTransaction()
    }

    fun getTransaction() {
        viewModelScope.launch {
            mainRepository.getTransaction().collect {
                _resultTransaction.value = it
            }
        }
    }
}