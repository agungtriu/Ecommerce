package com.agungtriu.ecommerce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.data.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryImp: RepositoryImp) :
    ViewModel() {
    fun getStatusLogin(): LiveData<LoginModel> {
        return repositoryImp.getLoginStatus().asLiveData()
    }

    fun getThemeLang(): LiveData<ThemeLangModel> {
        return repositoryImp.getThemeLang().asLiveData()
    }
}