package com.agungtriu.ecommerce.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val _resultNotifications = MutableLiveData<List<NotificationEntity>?>()
    val resultNotifications: LiveData<List<NotificationEntity>?> get() = _resultNotifications

    fun selectNotifications() {
        viewModelScope.launch {
            mainRepository.selectNotifications().collect {
                _resultNotifications.value = it
            }
        }
    }

    fun updateNotifications(notificationEntity: NotificationEntity) {
        viewModelScope.launch {
            mainRepository.updateNotification(notificationEntity)
        }
    }
}