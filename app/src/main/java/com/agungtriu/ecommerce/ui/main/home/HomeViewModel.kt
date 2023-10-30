package com.agungtriu.ecommerce.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.data.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repositoryImp: RepositoryImp) : ViewModel() {

    fun doLogout() {
        viewModelScope.launch {
            repositoryImp.deleteLoginStatus()
        }
    }

    fun changeTheme(isDark: Boolean) {
        viewModelScope.launch {
            repositoryImp.changeTheme(isDark)
        }
    }

    fun changeLanguage(language: String) {
        viewModelScope.launch {
            repositoryImp.changeLang(language)
        }
    }

    fun getThemeLang(): LiveData<ThemeLangModel> {
        return repositoryImp.getThemeLang().asLiveData()
    }
}