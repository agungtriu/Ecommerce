package com.agungtriu.ecommerce.ui.prelogin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val preLoginRepository: PreLoginRepository) :
    ViewModel() {
    fun getOnBoardingStatus(): Boolean {
        return runBlocking {
            preLoginRepository.getOnboardingStatus().first()
        }
    }

    private val _resultLogin = MutableLiveData<ViewState<DataLogin>>()
    val resultLogin: LiveData<ViewState<DataLogin>> get() = _resultLogin

    fun postLogin(email: String, password: String) {
        viewModelScope.launch {
            preLoginRepository.postLogin(
                RequestLogin(
                    email = email,
                    password = password,
                    firebaseToken = preLoginRepository.getFirebaseToken()
                )
            ).collect {
                _resultLogin.value = it
            }
        }
    }
}
