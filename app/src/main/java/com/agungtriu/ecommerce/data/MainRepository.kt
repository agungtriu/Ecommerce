package com.agungtriu.ecommerce.data

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
    suspend fun refreshToken(refreshTokenModel: TokenModel)
    suspend fun changeTheme(isDark: Boolean)
    suspend fun changeLang(language: String)
    fun getThemeLang(): Flow<ThemeLangModel>


    suspend fun registerProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>>
}