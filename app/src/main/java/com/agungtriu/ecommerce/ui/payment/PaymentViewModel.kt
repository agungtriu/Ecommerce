package com.agungtriu.ecommerce.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getFirebasePayment(): LiveData<ViewState<List<DataTypePayment>>> =
        mainRepository.getFirebasePayments()

    fun updateFirebasePayment(): LiveData<ViewState<List<DataTypePayment>>> =
        mainRepository.updateFirebasePayments()

}