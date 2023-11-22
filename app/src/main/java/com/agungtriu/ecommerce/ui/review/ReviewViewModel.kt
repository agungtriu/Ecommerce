package com.agungtriu.ecommerce.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.review.ReviewFragment.Companion.REVIEW_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = savedStateHandle[REVIEW_KEY] ?: ""

    private var _resultReview = MutableLiveData<ViewState<List<DataReview>>>()
    val resultReview: LiveData<ViewState<List<DataReview>>> get() = _resultReview

    init {
        getReviewsByProductId()
    }

    private fun getReviewsByProductId() {
        viewModelScope.launch {
            storeRepository.getReviewsByProductId(productId).collect {
                _resultReview.value = it
            }
        }
    }
}