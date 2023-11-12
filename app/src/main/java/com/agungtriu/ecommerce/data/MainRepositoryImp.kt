package com.agungtriu.ecommerce.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImp @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : MainRepository {
    override suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel) {
        return dataStoreManager.updateLoginStatus(registerProfileModel = registerProfileModel)
    }

    override suspend fun refreshToken(refreshTokenModel: TokenModel) {
        return dataStoreManager.refreshToken(refreshTokenModel = refreshTokenModel)
    }

    override suspend fun changeTheme(isDark: Boolean) {
        dataStoreManager.changeTheme(isDark = isDark)
    }

    override suspend fun changeLang(language: String) {
        dataStoreManager.changeLanguage(language = language)
    }

    override fun getThemeLang(): Flow<ThemeLangModel> {
        return dataStoreManager.getThemeLang()
    }

    override fun getAuthorizedStatus(): Flow<AuthorizeModel> {
        return dataStoreManager.getAuthorizedStatus()
    }

    override suspend fun deleteLoginStatus() {
        return dataStoreManager.deleteLoginStatus()
    }

    override suspend fun registerProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.registerProfile(
                    requestProfile.userName, requestProfile.userImage
                )
                val dataRegister = result.data
                if (dataRegister != null) {
                    dataStoreManager.updateLoginStatus(
                        RegisterProfileModel(
                            userName = dataRegister.userName,
                            userImage = dataRegister.userImage,
                        )
                    )
                    emit(ViewState.Success(dataRegister))
                } else {
                    throw Exception("Data register profile not found")
                }
            } catch (t: Throwable) {
                emit(ViewState.Error(t.toResponseError()))
            }
        }

    override fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1),
        pagingSourceFactory = {
            ProductsPagingSource(apiService, requestProducts)
        },
    ).flow

    override fun getDetailProduct(productId: String): Flow<ViewState<DataDetailProduct>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.getDetailProduct(productId = productId)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data Product not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override fun getReviewProduct(productId: String): Flow<ViewState<List<DataReview>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.getReviewProduct(productId)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data review not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override suspend fun doSearch(query: String): Flow<ViewState<List<String>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.doSearch(query = query)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data search not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }

    override suspend fun insertWishlist(wishlistEntity: WishlistEntity) {
        appDatabase.wishlistDao().insertWishlist(wishlistEntity)
    }

    override suspend fun deleteWishlist(id: String) {
        appDatabase.wishlistDao().deleteWishlistById(id)
    }

    override fun getWishlists(): Flow<List<WishlistEntity>> =
        appDatabase.wishlistDao().selectWishlists()

    override fun getWishlistById(id: String): Flow<WishlistEntity> =
        appDatabase.wishlistDao().selectWishlistById(id)

    override fun insertCart(cartEntity: CartEntity): Flow<ViewState<String>> = flow {
        val check = appDatabase.cartDao().selectCartById(cartEntity.id).first()
        if (check != null) {
            if ((check.stock ?: 0) > (check.quantity ?: 0)) {
                updateCart(
                    cartEntity = CartEntity(
                        id = cartEntity.id,
                        image = cartEntity.image,
                        productName = cartEntity.productName,
                        productPrice = cartEntity.productPrice,
                        store = cartEntity.store,
                        stock = cartEntity.stock,
                        variantPrice = cartEntity.variantPrice,
                        variantName = cartEntity.variantName,
                        quantity = check.quantity?.plus(1),
                        isSelected = check.isSelected
                    )
                )
                emit(ViewState.Success("quantity"))
            } else {
                emit(ViewState.Error(ResponseError(404, "stock tidak tersedia")))
            }
        } else {
            appDatabase.cartDao().insertCart(cartEntity)
            emit(ViewState.Success("cart"))
        }
    }

    override suspend fun updateCart(cartEntity: CartEntity) {
        appDatabase.cartDao().updateCart(cartEntity)
    }

    override suspend fun updateAllCartSelected(isSelected: Boolean) {
        appDatabase.cartDao().updateCartsSelected(isSelected)
    }

    override suspend fun deleteCart(cartEntity: CartEntity) {
        appDatabase.cartDao().deleteCart(cartEntity)
    }

    override suspend fun deleteAllCartSelected() {
        appDatabase.cartDao().deleteCartsSelected()
    }

    override fun getAllCart(): Flow<List<CartEntity>?> = appDatabase.cartDao().selectCarts()

    override fun checkCartIsSelected(isSelected: Boolean): Flow<List<CartEntity>> =
        appDatabase.cartDao().selectCartsIsSelected(isSelected)

    override fun getCartById(id: String): Flow<CartEntity?> =
        appDatabase.cartDao().selectCartById(id)

    override fun getTotalPay(): Flow<Int?> = appDatabase.cartDao().selectTotalPay()
    override fun getQuantity(): Flow<Int?> = appDatabase.cartDao().selectQuantity()
}