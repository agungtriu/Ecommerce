package com.agungtriu.ecommerce.data

import androidx.lifecycle.LiveData
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.response.DataFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {
    fun getPayments(): Flow<ViewState<List<DataTypePayment>>>
    fun getFirebasePayments(): LiveData<ViewState<List<DataTypePayment>>>
    fun updateFirebasePayments(): LiveData<ViewState<List<DataTypePayment>>>
    suspend fun postFulfillment(requestFulfillment: RequestFulfillment): Flow<ViewState<DataFulfillment>>
    suspend fun postRating(requestRating: RequestRating): Flow<ViewState<String>>
    suspend fun getTransactions(): Flow<ViewState<List<DataTransaction>>>

}