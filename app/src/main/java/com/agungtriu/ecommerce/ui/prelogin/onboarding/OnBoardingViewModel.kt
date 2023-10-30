package com.agungtriu.ecommerce.ui.prelogin.onboarding

import androidx.lifecycle.ViewModel
import com.agungtriu.ecommerce.data.PreLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val preLoginRepository: PreLoginRepository) :
    ViewModel() {

    fun saveOnBoardingStatus() {
        runBlocking {
            preLoginRepository.saveOnboarding()
        }
    }
}