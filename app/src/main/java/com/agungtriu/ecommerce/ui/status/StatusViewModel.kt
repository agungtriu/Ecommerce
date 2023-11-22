package com.agungtriu.ecommerce.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.data.CheckoutRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.status.StatusFragment.Companion.STATE_STATUS_KEY
import com.agungtriu.ecommerce.ui.status.StatusFragment.Companion.STATUS_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val dataFulfillment = savedStateHandle[STATUS_KEY] ?: StatusModel()
    val stateStatus = savedStateHandle[STATE_STATUS_KEY] ?: ""

    fun postRating(requestRating: RequestRating): LiveData<ViewState<String>> =
        runBlocking { checkoutRepository.postRating(requestRating).asLiveData() }
}