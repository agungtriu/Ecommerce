package com.agungtriu.ecommerce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) :
    ViewModel() {

    fun getAuthorizedStatus(): LiveData<AuthorizeModel> {
        return mainRepository.getAuthorizedStatus().asLiveData()
    }

    fun getThemeLang(): LiveData<ThemeLangModel> {
        return mainRepository.getThemeLang().asLiveData()
    }
}
