package com.agungtriu.ecommerce.ui.prelogin.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var preLoginRepository: PreLoginRepository

    @Before
    fun setUp() {
        preLoginRepository = mock()
        registerViewModel = RegisterViewModel(preLoginRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultRegister_success() = runTest {
        whenever(preLoginRepository.getFirebaseToken()).thenReturn("")
        whenever(
            preLoginRepository.postRegister(
                requestRegister = RequestRegister(
                    "",
                    "",
                    ""
                )
            )
        ).thenReturn(
            flowOf(ViewState.Loading, ViewState.Success(DataDummy.dummyRegisterResponse.data!!))
        )
        registerViewModel.postRegister("", "")
        val actual = mutableListOf<ViewState<DataRegister>>()
        registerViewModel.resultRegister.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyRegisterResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultRegister_error() = runTest {
        whenever(preLoginRepository.getFirebaseToken()).thenReturn("")
        whenever(
            preLoginRepository.postRegister(
                requestRegister = RequestRegister(
                    "",
                    "",
                    ""
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyRegisterResponseEmailAlreadyRegister)
            )
        )
        registerViewModel.postRegister("", "")
        val actual = mutableListOf<ViewState<DataRegister>>()
        registerViewModel.resultRegister.observeForever {
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
}
