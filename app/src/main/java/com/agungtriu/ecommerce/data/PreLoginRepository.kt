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
    suspend fun saveOnboarding()
    fun getLoginData(): Flow<LoginModel>
    suspend fun saveLoginData(loginModel: LoginModel)

    suspend fun doRegister(requestRegister: RequestRegister): Flow<ViewState<DataRegister>>
    suspend fun doLogin(requestLogin: RequestLogin): Flow<ViewState<DataLogin>>

}