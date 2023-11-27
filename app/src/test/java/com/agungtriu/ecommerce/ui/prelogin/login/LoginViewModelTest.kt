package com.agungtriu.ecommerce.ui.prelogin.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
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
class LoginViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var preLoginRepository: PreLoginRepository

    @Before
    fun setUp() {
        preLoginRepository = mock()
        loginViewModel = LoginViewModel(preLoginRepository)
    }

    @Test
    fun getOnBoardingStatus() {
        whenever(preLoginRepository.getOnboardingStatus()).thenReturn(flowOf(true))
        val actual = loginViewModel.getOnBoardingStatus()
        assertEquals(true, actual)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultLogin_success() = runTest {
        whenever(preLoginRepository.getFirebaseToken()).thenReturn("")
        whenever(
            preLoginRepository.postLogin(
                RequestLogin(
                    "",
                    "",
                    ""
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyLoginResponse.data!!)
            )
        )

        loginViewModel.postLogin(email = "", password = "")

        val actual = mutableListOf<ViewState<DataLogin>>()
        loginViewModel.resultLogin.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyLoginResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultLogin_error() = runTest {
        whenever(preLoginRepository.getFirebaseToken()).thenReturn("")
        whenever(
            preLoginRepository.postLogin(
                RequestLogin(
                    "",
                    "",
                    ""
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError400Response)
            )
        )

        loginViewModel.postLogin(email = "", password = "")

        val actual = mutableListOf<ViewState<DataLogin>>()
        loginViewModel.resultLogin.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError400Response)
            ),
            actual
        )
    }
}