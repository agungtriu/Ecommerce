package com.agungtriu.ecommerce.data

import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun deleteLoginStatus()
    suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel)
    suspend fun refreshToken(refreshTokenModel: TokenModel)
    suspend fun changeTheme(isDark: Boolean)
    suspend fun changeLang(language: String)
    fun getThemeLang(): Flow<ThemeLangModel>

    fun getAuthorizedStatus(): Flow<AuthorizeModel>

    suspend fun registerProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>>

    fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>>

    fun getDetailProduct(productId: String): Flow<ViewState<DataDetailProduct>>

    fun getReviewProduct(productId: String): Flow<ViewState<List<DataReview>>>

    suspend fun doSearch(query: String): Flow<ViewState<List<String>>>

    suspend fun insertWishlist(wishlistEntity: WishlistEntity)

    suspend fun deleteWishlist(id: String)

    fun getWishlists(): Flow<List<WishlistEntity>>

    fun getWishlistById(id: String): Flow<WishlistEntity>

    fun insertCart(cartEntity: CartEntity): Flow<ViewState<String>>

    suspend fun updateCart(cartEntity: CartEntity)

    suspend fun updateAllCartSelected(isSelected: Boolean)

    suspend fun deleteCart(cartEntity: CartEntity)

    suspend fun deleteAllCartSelected()
    fun getAllCart(): Flow<List<CartEntity>?>
    fun getCartById(id: String): Flow<CartEntity?>
    fun getQuantity(): Flow<Int?>
}