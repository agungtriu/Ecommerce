package com.agungtriu.ecommerce.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun doLogout() {
        viewModelScope.launch {
            mainRepository.deleteLoginStatus()
        }
    }

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            mainRepository.setTheme(isDark)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            mainRepository.setLanguage(language)
        }
    }

    fun getThemeLang(): LiveData<ThemeLangModel> {
        return mainRepository.getThemeLang().asLiveData()
    }
}
