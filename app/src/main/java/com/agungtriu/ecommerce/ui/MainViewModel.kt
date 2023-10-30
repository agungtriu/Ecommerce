package com.agungtriu.ecommerce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.data.PreLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preLoginRepository: PreLoginRepository,
    private val mainRepository: MainRepository
) :
    ViewModel() {
    fun getStatusLogin(): LiveData<LoginModel> {
        return preLoginRepository.getLoginStatus().asLiveData()
    }

    fun getThemeLang(): LiveData<ThemeLangModel> {
        return mainRepository.getThemeLang().asLiveData()
    }
}