package com.agungtriu.ecommerce.data

import androidx.lifecycle.LiveData
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.response.DataFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.data.firebase.RemoteConfig
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckoutRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val remoteConfig: RemoteConfig
) :
    CheckoutRepository {

    override fun getPayments(): Flow<ViewState<List<DataTypePayment>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.getPayments()
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override fun getFirebasePayments(): LiveData<ViewState<List<DataTypePayment>>> =
        remoteConfig.getPayment()

    override fun updateFirebasePayments(): LiveData<ViewState<List<DataTypePayment>>> =
        remoteConfig.updatePayment()

    override suspend fun postFulfillment(requestFulfillment: RequestFulfillment): Flow<ViewState<DataFulfillment>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.postFulfillment(requestFulfillment = requestFulfillment)
                val data = result.data
                if (data != null) {
                    emit(ViewState.Success(data))
                }
            } catch (t: Throwable) {
                emit(ViewState.Error(t.toResponseError()))
            }
        }

    override suspend fun postRating(requestRating: RequestRating): Flow<ViewState<String>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.postRating(requestRating = requestRating)
            val data = result.message
            if (data != null) {
                emit(ViewState.Success(data))
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override suspend fun getTransactions(): Flow<ViewState<List<DataTransaction>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.getTransactions()
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }
}
