package com.agungtriu.ecommerce.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}