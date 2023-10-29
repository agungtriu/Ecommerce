package com.agungtriu.ecommerce.ui.prelogin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.data.RepositoryImp
import com.agungtriu.ecommerce.helper.Config.FIREBASE_TOKEN
import com.agungtriu.ecommerce.helper.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: RepositoryImp) : ViewModel() {
    fun getOnBoardingStatus(): Boolean {
        return runBlocking {
            repository.getOnboardingStatus().first()
        }
    }

    private val _resultLogin = MutableLiveData<ViewState<DataLogin>>()
    val resultLogin: LiveData<ViewState<DataLogin>> get() = _resultLogin

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.doLogin(
                RequestLogin(
                    email = email,
                    password = password,
                    firebaseToken = FIREBASE_TOKEN
                )
            ).collect {
                _resultLogin.value = it
            }
        }
    }
}