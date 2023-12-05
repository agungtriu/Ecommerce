package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.data.firebase.FirebaseMessaging
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.Extension.toHttpException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class PreLoginRepositoryTest {

    private lateinit var preLoginRepository: PreLoginRepository
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var apiService: ApiService
    private lateinit var firebaseMessaging: FirebaseMessaging

    @Before
    fun setUp() {
        dataStoreManager = mock()
        apiService = mock()
        firebaseMessaging = mock()
        preLoginRepository = PreLoginRepositoryImp(dataStoreManager, apiService, firebaseMessaging)
    }

    @Test
    fun getOnboardingStatus() = runTest {
        whenever(dataStoreManager.getOnboardingStatus()).thenReturn(flowOf(true))
        val actual = preLoginRepository.getOnboardingStatus().first()
        Assert.assertEquals(true, actual)
    }

    @Test
    fun getLoginStatus() = runTest {
        whenever(dataStoreManager.getLoginStatus()).thenReturn(flowOf(false))
        val actual = preLoginRepository.getLoginStatus().first()
        Assert.assertEquals(false, actual)
    }

    @Test
    fun getLoginData() = runTest {
        whenever(dataStoreManager.getLoginData()).thenReturn(flowOf(DataDummy.dummyLoginModel))
        val actual = preLoginRepository.getLoginData().first()
        Assert.assertEquals(DataDummy.dummyLoginModel, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postRegister_success() = runTest {
        whenever(
            apiService.postRegister(
                requestRegister = RequestRegister(
                    "",
                    "",
                    ""
                )
            )
        ).thenReturn(DataDummy.dummyRegisterResponse)

        val actual = mutableListOf<ViewState<DataRegister>>()
        preLoginRepository.postRegister(RequestRegister("", "", "")).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyRegisterResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postRegister_error() = runTest {
        whenever(
            apiService.postRegister(
                requestRegister = RequestRegister(
                    "",
                    "",
                    ""
                )
            )
        ).thenThrow(DataDummy.dummyRegisterResponseEmailAlreadyRegister.toHttpException())

        val actual = mutableListOf<ViewState<DataRegister>>()
        preLoginRepository.postRegister(RequestRegister("", "", "")).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyRegisterResponseEmailAlreadyRegister)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postLogin_success() = runTest {
        whenever(
            apiService.postLogin(
                requestLogin = RequestLogin(
                    "",
                    "",
                    ""
                )
            )
        ).thenReturn(DataDummy.dummyLoginResponse)

        val actual = mutableListOf<ViewState<DataLogin>>()
        preLoginRepository.postLogin(RequestLogin("", "", "")).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyLoginResponse.data)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postLogin_error() = runTest {
        whenever(
            apiService.postLogin(
                requestLogin = RequestLogin(
                    "",
                    "",
                    ""
                )
            )
        ).thenThrow(DataDummy.dummyError400Response.toHttpException())

        val actual = mutableListOf<ViewState<DataLogin>>()
        preLoginRepository.postLogin(RequestLogin("", "", "")).collect {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError400Response)
            ),
            actual
        )
    }
}
