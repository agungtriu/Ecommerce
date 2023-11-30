package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun insertNotification(notificationEntity: NotificationEntity)
    fun selectNotifications(): Flow<List<NotificationEntity>?>
    suspend fun updateNotification(notificationEntity: NotificationEntity)
}
