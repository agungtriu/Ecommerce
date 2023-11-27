package com.agungtriu.ecommerce.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.data.PreLoginRepository
import com.agungtriu.ecommerce.data.WishlistRepository
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
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var preLoginRepository: PreLoginRepository
    private lateinit var mainRepository: MainRepository
    private lateinit var wishlistRepository: WishlistRepository

    @Before
    fun setUp() {
        preLoginRepository = mock()
        mainRepository = mock()
        wishlistRepository = mock()
        mainViewModel = MainViewModel(preLoginRepository, mainRepository, wishlistRepository)
    }

    @Test
    fun getLoginData() = runTest {
        whenever(preLoginRepository.getLoginData()).thenReturn(flowOf(DataDummy.dummyLoginModel))
        mainViewModel.getLoginData().observeForever {
            assertEquals(DataDummy.dummyLoginModel, it)
        }
    }

    @Test
    fun getWishlists_success_notNull() {
        whenever(wishlistRepository.getWishlists()).thenReturn(flowOf(listOf(DataDummy.dummyWishlistEntity)))
        mainViewModel.getWishlists().observeForever {
            assertEquals(listOf(DataDummy.dummyWishlistEntity), it)
        }
    }

    @Test
    fun getWishlists_success_null() {
        whenever(wishlistRepository.getWishlists()).thenReturn(flowOf(null))
        mainViewModel.getWishlists().observeForever {
            assertEquals(null, it)
        }
    }

    @Test
    fun selectCountCart_success_notNull() {
        whenever(mainRepository.selectCountCart()).thenReturn(flowOf(1))
        mainViewModel.selectCountCart().observeForever {
            assertEquals(1, it)
        }
    }

    @Test
    fun selectCountCart_success_null() {
        whenever(mainRepository.selectCountCart()).thenReturn(flowOf(null))
        mainViewModel.selectCountCart().observeForever {
            assertEquals(null, it)
        }
    }

    @Test
    fun selectCountNotification_success_notNull() {
        whenever(mainRepository.selectCountNotification()).thenReturn(flowOf(1))
        mainViewModel.selectCountNotification().observeForever {
            assertEquals(1, it)
        }
    }
    @Test
    fun selectCountNotification_success_null() {
        whenever(mainRepository.selectCountNotification()).thenReturn(flowOf(null))
        mainViewModel.selectCountNotification().observeForever {
            assertEquals(null, it)
        }
    }
}