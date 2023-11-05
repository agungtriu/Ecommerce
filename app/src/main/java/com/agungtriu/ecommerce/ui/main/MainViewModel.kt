package com.agungtriu.ecommerce.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.data.PreLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preLoginRepository: PreLoginRepository) : ViewModel() {

    fun getLoginStatus(): LiveData<LoginModel> {
        return preLoginRepository.getDataLogin().asLiveData()
    }
}