package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val appDatabase: AppDatabase
) : NotificationRepository {

    override suspend fun insertNotification(notificationEntity: NotificationEntity) {
        appDatabase.notificationDao().insertNotification(notificationEntity)
    }

    override fun selectNotifications(): Flow<List<NotificationEntity>?> =
        appDatabase.notificationDao().selectNotifications()

    override suspend fun updateNotification(notificationEntity: NotificationEntity) {
        appDatabase.notificationDao().updateNotification(notificationEntity)
    }
}