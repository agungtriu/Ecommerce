package com.agungtriu.ecommerce.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.flow.flowOf
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
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainRepository: MainRepository
    private lateinit var preLoginRepository: PreLoginRepository

    @Before
    fun setUp() {
        mainRepository = mock()
        preLoginRepository = mock()
        mainViewModel = MainViewModel(mainRepository, preLoginRepository)
    }

    @Test
    fun getLoginStatus() = runTest {
        whenever(preLoginRepository.getLoginData()).thenReturn(flowOf(DataDummy.dummyLoginModel))
        val actual = mainViewModel.getLoginStatus()
        Assert.assertEquals(DataDummy.dummyLoginModel.isLogin, actual)
    }

    @Test
    fun getAuthorizedStatus() = runTest {
        whenever(mainRepository.getAuthorizedStatus()).thenReturn(flowOf(DataDummy.dummyAuthorizeModel))
        mainViewModel.getAuthorizedStatus().observeForever {
            Assert.assertEquals(DataDummy.dummyAuthorizeModel, it)
        }
    }

    @Test
    fun getThemeLang() = runTest {
        whenever(mainRepository.getThemeLang()).thenReturn(flowOf(DataDummy.dummyThemeLangModel))
        mainViewModel.getThemeLang().observeForever {
            Assert.assertEquals(DataDummy.dummyThemeLangModel, it)
        }
    }
}
