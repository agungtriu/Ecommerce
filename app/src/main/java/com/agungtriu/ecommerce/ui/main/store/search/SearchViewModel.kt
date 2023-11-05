package com.agungtriu.ecommerce.ui.main.store.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private var _result = MutableLiveData<ViewState<List<String>>>()
    val result: LiveData<ViewState<List<String>>> get() = _result

    private fun doSearch(query: String) {
        viewModelScope.launch {
            mainRepository.doSearch(query).collect {
                _result.value = it
            }
        }
    }

    private var searchJob: Job? = null
    fun searchDebounced(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1000)
            doSearch(query)
        }
    }
}