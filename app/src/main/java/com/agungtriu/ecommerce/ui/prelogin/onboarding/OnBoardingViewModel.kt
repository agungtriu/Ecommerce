package com.agungtriu.ecommerce.ui.prelogin.onboarding

import androidx.lifecycle.ViewModel
import com.agungtriu.ecommerce.data.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val repository: RepositoryImp) : ViewModel() {
    fun saveOnBoardingStatus() {
        runBlocking(Dispatchers.IO) {
            repository.saveOnboarding()
        }
    }
}