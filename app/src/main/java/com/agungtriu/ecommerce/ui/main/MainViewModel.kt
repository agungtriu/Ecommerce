package com.agungtriu.ecommerce.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.data.PreLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preLoginRepository: PreLoginRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getLoginData(): LiveData<LoginModel> {
        return preLoginRepository.getLoginData().asLiveData()
    }

    fun getWishlists(): LiveData<List<WishlistEntity>> = mainRepository.getWishlists().asLiveData()
    fun getQuantity(): LiveData<Int?> = mainRepository.getQuantity().asLiveData()
}