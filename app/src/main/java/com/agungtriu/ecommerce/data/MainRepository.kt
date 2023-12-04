package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun deleteLoginStatus()
    suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel)
    suspend fun setRefreshToken(refreshTokenModel: TokenModel)
    suspend fun setTheme(isDark: Boolean)
    suspend fun setLanguage(language: String)
    fun getThemeLang(): Flow<ThemeLangModel>
    fun getAuthorizedStatus(): Flow<AuthorizeModel>
    suspend fun postProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>>
    fun selectCountCart(): Flow<Int>
    fun selectCountNotification(): Flow<Int>
}
