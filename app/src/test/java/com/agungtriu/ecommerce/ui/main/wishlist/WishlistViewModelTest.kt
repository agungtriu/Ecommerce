package com.agungtriu.ecommerce.ui.main.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.data.WishlistRepository
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
class WishlistViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var wishlistViewModel: WishlistViewModel
    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        wishlistRepository = mock()
        cartRepository = mock()
        wishlistViewModel = WishlistViewModel(wishlistRepository, cartRepository)
    }

    @Test
    fun getResultWishlist_success_notNull() = runTest {
        whenever(wishlistRepository.getWishlists()).thenReturn(flowOf(listOf(DataDummy.dummyWishlistEntity)))
        wishlistViewModel.getWishlists()
        wishlistViewModel.resultWishlist.observeForever {
            Assert.assertEquals(listOf(DataDummy.dummyWishlistEntity), it)
        }
    }

    @Test
    fun getResultWishlist_success_null() = runTest {
        whenever(wishlistRepository.getWishlists()).thenReturn(flowOf(null))
        wishlistViewModel.getWishlists()
        wishlistViewModel.resultWishlist.observeForever {
            Assert.assertEquals(null, it)
        }
    }
}
