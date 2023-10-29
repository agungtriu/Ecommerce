package com.agungtriu.ecommerce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStoreManager: DataStoreManager) :
    ViewModel() {
    fun getStatusLogin(): LiveData<LoginModel> {
        return dataStoreManager.getLoginStatus().asLiveData()
    }
}