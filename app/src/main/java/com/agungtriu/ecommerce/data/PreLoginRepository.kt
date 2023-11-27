package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow

interface PreLoginRepository {
    fun getOnboardingStatus(): Flow<Boolean>
    suspend fun setOnboarding()
    fun getLoginData(): Flow<LoginModel>
    suspend fun setLoginData(loginModel: LoginModel)

    suspend fun postRegister(requestRegister: RequestRegister): Flow<ViewState<DataRegister>>
    suspend fun postLogin(requestLogin: RequestLogin): Flow<ViewState<DataLogin>>

    suspend fun getFirebaseToken(): String

}