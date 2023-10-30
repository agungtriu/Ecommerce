package com.agungtriu.ecommerce.ui.prelogin.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.helper.Config.FIREBASE_TOKEN
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val preLoginRepository: PreLoginRepository) :
    ViewModel() {

    private val _resultRegister = MutableLiveData<ViewState<DataRegister>>()
    val resultRegister: LiveData<ViewState<DataRegister>> = _resultRegister

    fun doRegister(email: String, password: String) {
        viewModelScope.launch {
            preLoginRepository.doRegister(
                RequestRegister(
                    email = email,
                    password = password,
                    firebaseToken = FIREBASE_TOKEN
                )
            ).collect {
                _resultRegister.value = it
            }
        }
    }
}