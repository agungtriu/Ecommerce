package com.agungtriu.ecommerce.ui.notification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.data.NotificationRepository
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class NotificationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationRepository: NotificationRepository

    @Before
    fun setUp() {
        notificationRepository = mock()
        notificationViewModel = NotificationViewModel(notificationRepository)
    }

    @Test
    fun getResultNotifications_success_notNull() = runTest {
        whenever(notificationRepository.selectNotifications()).thenReturn(flowOf(listOf(DataDummy.dummyNotificationEntity)))
        notificationViewModel.selectNotifications()
        notificationViewModel.resultNotifications.observeForever {
            assertEquals(listOf(DataDummy.dummyNotificationEntity), it)
        }
    }

    @Test
    fun getResultNotifications_success_null() = runTest {
        whenever(notificationRepository.selectNotifications()).thenReturn(flowOf(null))
        notificationViewModel.selectNotifications()
        notificationViewModel.resultNotifications.observeForever {
            assertEquals(null, it)
        }
    }
}