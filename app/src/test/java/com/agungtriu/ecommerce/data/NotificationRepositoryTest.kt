package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.NotificationDao
import com.agungtriu.ecommerce.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class NotificationRepositoryTest {

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var notificationDao: NotificationDao

    @Before
    fun setUp() {
        appDatabase = mock()
        notificationDao = mock()
        notificationRepository = NotificationRepositoryImp(appDatabase)
    }

    @Test
    fun selectNotifications_success_notNull() = runTest {
        whenever(appDatabase.notificationDao()).thenReturn(notificationDao)
        whenever(appDatabase.notificationDao().selectNotifications()).thenReturn(
            flowOf(
                listOf(
                    DataDummy.dummyNotificationEntity
                )
            )
        )

        val actual = notificationRepository.selectNotifications().first()
        Assert.assertEquals(listOf(DataDummy.dummyNotificationEntity), actual)
    }

    @Test
    fun selectNotifications_success_null() = runTest {
        whenever(appDatabase.notificationDao()).thenReturn(notificationDao)
        whenever(appDatabase.notificationDao().selectNotifications()).thenReturn(
            flowOf(
                null
            )
        )

        val actual = notificationRepository.selectNotifications().first()
        Assert.assertEquals(null, actual)
    }
}
