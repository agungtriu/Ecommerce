package com.agungtriu.ecommerce.core.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String? = null,
    val body: String? = null,
    val image: String? = null,
    val date: String? = null,
    val time: String? = null,
    val type: String? = null,
    val isRead: Boolean = false
)
