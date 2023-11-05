package com.agungtriu.ecommerce.data

import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.datastore.model.LoginStatusModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun deleteLoginStatus()
    suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel)
    suspend fun refreshToken(refreshTokenModel: TokenModel)
    suspend fun changeTheme(isDark: Boolean)
    suspend fun changeLang(language: String)
    fun getThemeLang(): Flow<ThemeLangModel>

    fun getLoginStatus(): Flow<LoginStatusModel>

    suspend fun registerProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>>

    fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>>

    suspend fun doSearch(query: String): Flow<ViewState<List<String>>>
}