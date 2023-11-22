package com.agungtriu.ecommerce.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.data.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepository: NotificationRepository) :
    ViewModel() {

    private val _resultNotifications = MutableLiveData<List<NotificationEntity>?>()
    val resultNotifications: LiveData<List<NotificationEntity>?> get() = _resultNotifications

    fun selectNotifications() {
        viewModelScope.launch {
            notificationRepository.selectNotifications().collect {
                _resultNotifications.value = it
            }
        }
    }

    fun updateNotifications(notificationEntity: NotificationEntity) {
        viewModelScope.launch {
            notificationRepository.updateNotification(notificationEntity)
        }
    }
}