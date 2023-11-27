package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.CartDao
import com.agungtriu.ecommerce.core.room.NotificationDao
import com.agungtriu.ecommerce.helper.Language
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.Extension.toHttpException
import com.agungtriu.ecommerce.utils.Extension.toMultipartBodyPart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class MainRepositoryTest {

    private lateinit var mainRepository: MainRepository
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var apiService: ApiService
    private lateinit var appDatabase: AppDatabase
    private lateinit var cartDao: CartDao
    private lateinit var notificationDao: NotificationDao

    @Before
    fun setUp() {
        dataStoreManager = mock()
        apiService = mock()
        appDatabase = mock()
        cartDao = mock()
        notificationDao = mock()
        mainRepository = MainRepositoryImp(dataStoreManager, apiService, appDatabase)
    }

    @Test
    fun getThemeLang() = runTest {
        whenever(dataStoreManager.getThemeLang()).thenReturn(
            flowOf(
                ThemeLangModel(
                    isDark = true,
                    language = Language.en.name
                )
            )
        )
        val actual = mainRepository.getThemeLang().first()
        assertEquals(true, actual.isDark)
        assertEquals(Language.en.name, actual.language)
    }

    @Test
    fun getAuthorizedStatus() = runTest {
        whenever(dataStoreManager.getAuthorizedStatus()).thenReturn(
            flowOf(
                AuthorizeModel(
                    isAuthorized = true
                )
            )
        )
        val actual = mainRepository.getAuthorizedStatus().first()
        assertEquals(true, actual.isAuthorized)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postProfile_success() = runTest {
        val userNamePart = "".toMultipartBodyPart("userName")
        whenever(
            apiService.postProfile(userNamePart, null)
        ).thenReturn(DataDummy.dummyProfileResponse)
        val actual = mutableListOf<ViewState<DataProfile>>()
        mainRepository.postProfile(RequestProfile(userNamePart, null)).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProfileResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postProfile_error() = runTest {
        val userNamePart = "".toMultipartBodyPart("userName")
        whenever(
            apiService.postProfile(userNamePart, null)
        ).thenThrow(DataDummy.dummyError401Response.toHttpException())

        val actual = mutableListOf<ViewState<DataProfile>>()
        mainRepository.postProfile(RequestProfile(userNamePart, null)).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            ),
            actual
        )
    }

    @Test
    fun selectCountCart_success_notNull() = runTest {
        whenever(appDatabase.cartDao()).thenReturn(cartDao)
        whenever(appDatabase.cartDao().selectCountCart()).thenReturn(flowOf(1))
        val actual = mainRepository.selectCountCart().first()
        assertEquals(1, actual)
    }

    @Test
    fun selectCountCart_success_null() = runTest {
        whenever(appDatabase.cartDao()).thenReturn(cartDao)
        whenever(appDatabase.cartDao().selectCountCart()).thenReturn(flowOf(null))
        val actual = mainRepository.selectCountCart().first()
        assertEquals(null, actual)
    }

    @Test
    fun selectCountNotification_success_notNull() = runTest {
        whenever(appDatabase.notificationDao()).thenReturn(notificationDao)
        whenever(appDatabase.notificationDao().selectCountNotifications()).thenReturn(flowOf(1))
        val actual = mainRepository.selectCountNotification().first()
        assertEquals(1, actual)
    }
    @Test
    fun selectCountNotification_success_null() = runTest {
        whenever(appDatabase.notificationDao()).thenReturn(notificationDao)
        whenever(appDatabase.notificationDao().selectCountNotifications()).thenReturn(flowOf(null))
        val actual = mainRepository.selectCountNotification().first()
        assertEquals(null, actual)
    }
}