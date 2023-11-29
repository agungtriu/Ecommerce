package com.agungtriu.ecommerce.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun selectNotifications(): Flow<List<NotificationEntity>?>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead == 0")
    fun selectCountNotifications(): Flow<Int?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotification(notificationEntity: NotificationEntity)
}
