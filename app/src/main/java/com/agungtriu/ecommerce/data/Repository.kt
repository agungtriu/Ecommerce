package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getOnboardingStatus(): Flow<Boolean>
    suspend fun saveOnboarding()
    fun getLoginStatus(): Flow<LoginModel>
    suspend fun saveLoginStatus(loginModel: LoginModel)
    suspend fun deleteLoginStatus()
    suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel)
    suspend fun refreshToken(refreshTokenModel: TokenModel)


    suspend fun doRegister(requestRegister: RequestRegister): Flow<ViewState<DataRegister>>
    suspend fun doLogin(requestLogin: RequestLogin): Flow<ViewState<DataLogin>>
    suspend fun registerProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>>

}