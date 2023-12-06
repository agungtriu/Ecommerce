package com.agungtriu.ecommerce.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.data.WishlistRepository
import com.agungtriu.ecommerce.ui.status.StatusFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preLoginRepository: PreLoginRepository,
    private val mainRepository: MainRepository,
    private val wishlistRepository: WishlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _stateTransaction: String? = savedStateHandle[StatusFragment.STATE_STATUS_KEY]
    val stateTransaction: String? get() = _stateTransaction

    fun setStateTransaction(state: String?) {
        _stateTransaction = state
    }

    fun getLoginStatus(): Boolean {
        return runBlocking {
            preLoginRepository.getLoginStatus().first()
        }
    }

    fun getLoginData(): LiveData<LoginModel> {
        return preLoginRepository.getLoginData().asLiveData()
    }

    fun getWishlists(): LiveData<List<WishlistEntity>?> =
        wishlistRepository.getWishlists().asLiveData()

    fun selectCountCart(): LiveData<Int> = mainRepository.selectCountCart().asLiveData()

    fun selectCountNotification(): LiveData<Int> =
        mainRepository.selectCountNotification().asLiveData()
}
