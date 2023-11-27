package com.agungtriu.ecommerce.core.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.agungtriu.ecommerce.core.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [33])
class NotificationDaoTest {
    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase
    private lateinit var notificationDao: NotificationDao

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        notificationDao = appDatabase.notificationDao()
    }

    @After
    fun close() {
        appDatabase.close()
    }

    @Test
    fun insertAndSelectNotifications() = runTest {
        notificationDao.insertNotification(
            notificationEntity = DataDummy.dummyNotificationEntity
        )
        val actual = notificationDao.selectNotifications().first()
        assertEquals(listOf(DataDummy.dummyNotificationEntity), actual)
    }

    @Test
    fun insertSelectCountNotification() = runTest {
        notificationDao.insertNotification(
            notificationEntity = DataDummy.dummyNotificationEntity
        )
        val actual = notificationDao.selectCountNotifications().first()
        assertEquals(1, actual)
    }

    @Test
    fun insertUpdateAndSelectNotifications() = runTest {
        notificationDao.insertNotification(
            notificationEntity = DataDummy.dummyNotificationEntity
        )
        notificationDao.updateNotification(
            notificationEntity = DataDummy.dummyUpdateNotificationEntity
        )
        val actual = notificationDao.selectNotifications().first()
        assertEquals(DataDummy.dummyUpdateNotificationEntity, actual?.get(0))
    }
}