package com.agungtriu.ecommerce.ui.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private var _resultReview = MutableLiveData<ViewState<List<DataReview>>>()
    val resultReview: LiveData<ViewState<List<DataReview>>> get() = _resultReview

    fun getReviewProduct(productId: String) {
        viewModelScope.launch {
            mainRepository.getReviewProduct(productId = productId).collect {
                _resultReview.value = it
            }
        }
    }
}