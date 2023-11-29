package com.agungtriu.ecommerce.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _resultRegisterProfile = MutableLiveData<ViewState<DataProfile>>()
    val resultRegisterProfile: LiveData<ViewState<DataProfile>> get() = _resultRegisterProfile

    fun postProfile(requestProfile: RequestProfile) {
        viewModelScope.launch {
            mainRepository.postProfile(
                requestProfile = requestProfile
            ).collect {
                _resultRegisterProfile.value = it
            }
        }
    }
}
