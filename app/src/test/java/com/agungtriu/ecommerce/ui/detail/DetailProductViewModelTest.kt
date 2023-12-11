package com.agungtriu.ecommerce.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.data.CartRepository
import com.agungtriu.ecommerce.data.StoreRepository
import com.agungtriu.ecommerce.data.WishlistRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.detail.DetailProductFragment.Companion.PRODUCT_ID_KEY
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
class DetailProductViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var detailProductViewModel: DetailProductViewModel
    private lateinit var storeRepository: StoreRepository
    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var id: String

    @Before
    fun setUp() {
        storeRepository = mock()
        wishlistRepository = mock()
        cartRepository = mock()

        savedStateHandle = SavedStateHandle(mapOf(PRODUCT_ID_KEY to "asf"))
        id = savedStateHandle.get<String>(PRODUCT_ID_KEY)!!
        whenever(storeRepository.getProductById(id)).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProductByIdResponse.data!!)
            )
        )

        detailProductViewModel = DetailProductViewModel(
            storeRepository,
            wishlistRepository,
            cartRepository,
            savedStateHandle
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultDetail_success() = runTest {
        whenever(storeRepository.getProductById(id)).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProductByIdResponse.data!!)
            )
        )
        detailProductViewModel = DetailProductViewModel(
            storeRepository,
            wishlistRepository,
            cartRepository,
            savedStateHandle
        )
        val actual = mutableListOf<ViewState<DataDetailProduct>>()
        detailProductViewModel.resultDetail.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProductByIdResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getResultDetail_error() = runTest {
        val expectedError = flowOf(
            ViewState.Loading,
            ViewState.Error(DataDummy.dummyError404Response)
        )
        whenever(storeRepository.getProductById(id)).thenReturn(
            expectedError
        )
        detailProductViewModel = DetailProductViewModel(
            storeRepository,
            wishlistRepository,
            cartRepository,
            savedStateHandle
        )
        val actual = mutableListOf<ViewState<DataDetailProduct>>()
        detailProductViewModel.resultDetail.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError404Response)
            ),
            actual
        )
    }

    @Test
    fun getWishlistByProductId_success_found() = runTest {
        whenever(wishlistRepository.getWishlistById(id)).thenReturn(flowOf(DataDummy.dummyWishlistEntity))
        detailProductViewModel.getWishlistByProductId().observeForever {
            Assert.assertEquals(DataDummy.dummyWishlistEntity, it)
        }
    }

    @Test
    fun getWishlistByProductId_success_notFound() = runTest {
        whenever(wishlistRepository.getWishlistById(id)).thenReturn(flowOf(null))
        detailProductViewModel.getWishlistByProductId().observeForever {
            Assert.assertEquals(null, it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addCart_success_insert() = runTest {
        whenever(
            cartRepository.insertCart(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success("cart")
            )
        )

        val actual = mutableListOf<ViewState<String>>()
        detailProductViewModel.addCart(
            cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
        )
        detailProductViewModel.resultAddCart.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success("cart")
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addCart_success_update() = runTest {
        whenever(
            cartRepository.insertCart(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        ).thenReturn(
            flowOf(ViewState.Loading, ViewState.Success("quantity"))
        )

        val actual = mutableListOf<ViewState<String>>()
        detailProductViewModel.addCart(
            cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
        )
        detailProductViewModel.resultAddCart.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success("quantity")
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addCart_error() = runTest {
        whenever(
            cartRepository.insertCart(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(
                    ResponseError(404, "stock tidak tersedia")
                )
            )
        )

        val actual = mutableListOf<ViewState<String>>()
        detailProductViewModel.addCart(
            cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
        )
        detailProductViewModel.resultAddCart.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(
                    ResponseError(404, "stock tidak tersedia")
                )
            ),
            actual
        )
    }

    @Test
    fun getWishlistCompose_success_found() = runTest {
        whenever(wishlistRepository.getWishlistById(id)).thenReturn(flowOf(DataDummy.dummyWishlistEntity))
        val actual = detailProductViewModel.getWishlistCompose()
        Assert.assertEquals(DataDummy.dummyWishlistEntity, actual)
    }

    @Test
    fun getWishlistCompose_success_notFound() = runTest {
        whenever(wishlistRepository.getWishlistById(id)).thenReturn(flowOf(null))
        val actual = detailProductViewModel.getWishlistCompose()
        Assert.assertEquals(null, actual)
    }

    @Test
    fun getCartCompose_success_found() = runTest {
        whenever(cartRepository.getCartById(id)).thenReturn(flowOf(DataDummy.dummyCartEntity))
        val actual = detailProductViewModel.getCartCompose()
        Assert.assertEquals(DataDummy.dummyCartEntity, actual)
    }

    @Test
    fun getCartCompose_success_notFound() = runTest {
        whenever(cartRepository.getCartById(id)).thenReturn(flowOf(null))
        val actual = detailProductViewModel.getCartCompose()
        Assert.assertEquals(null, actual)
    }

    @Test
    fun addCartCompose_success_insert() = runTest {
        whenever(
            cartRepository.insertCart(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        ).thenReturn(
            flowOf(
                ViewState.Success("cart")
            )
        )

        val actual = mutableListOf<ViewState<String>>()
        detailProductViewModel.addCart(
            cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
        )

        actual.add(
            detailProductViewModel.addCartCompose(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        )
        Assert.assertEquals(
            listOf(
                ViewState.Success("cart")
            ),
            actual
        )
    }

    @Test
    fun addCartCompose_success_update() = runTest {
        whenever(
            cartRepository.insertCart(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        ).thenReturn(
            flowOf(ViewState.Success("quantity"))
        )

        val actual = mutableListOf<ViewState<String>>()
        actual.add(
            detailProductViewModel.addCartCompose(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        )
        Assert.assertEquals(
            listOf(
                ViewState.Success("quantity")
            ),
            actual
        )
    }

    @Test
    fun addCartCompose_error() = runTest {
        whenever(
            cartRepository.insertCart(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        ).thenReturn(
            flowOf(
                ViewState.Error(
                    ResponseError(404, "stock tidak tersedia")
                )
            )
        )

        val actual = mutableListOf<ViewState<String>>()
        actual.add(
            detailProductViewModel.addCartCompose(
                cartEntity = CartEntity("", "", "", 0, "", "", 0, 0, "", 0, false)
            )
        )
        Assert.assertEquals(
            listOf(
                ViewState.Error(
                    ResponseError(404, "stock tidak tersedia")
                )
            ),
            actual
        )
    }
}
