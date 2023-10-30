package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.helper.Utils.getApiErrorMessage
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreLoginRepositoryImp @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
) : PreLoginRepository {
    override fun getOnboardingStatus(): Flow<Boolean> {
        return dataStoreManager.getOnboardingStatus()
    }

    override suspend fun saveOnboarding() {
        dataStoreManager.saveOnboardingStatus()
    }

    override fun getLoginStatus(): Flow<LoginModel> {
        return dataStoreManager.getLoginStatus()
    }

    override suspend fun saveLoginStatus(loginModel: LoginModel) {
        dataStoreManager.saveLoginStatus(
            loginModel = loginModel
        )
    }

    override suspend fun doRegister(requestRegister: RequestRegister): Flow<ViewState<DataRegister>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.doRegister(
                    requestRegister = requestRegister
                )
                val dataRegister = result.data
                if (dataRegister != null) {
                    dataStoreManager.saveLoginStatus(
                        LoginModel(
                            isLogin = true,
                            userName = dataRegister.userName,
                            userImage = dataRegister.userImage,
                            accessToken = dataRegister.accessToken,
                            refreshToken = dataRegister.refreshToken
                        )
                    )
                    emit(ViewState.Success(dataRegister))
                } else {
                    throw Exception("Data register not found")
                }
            } catch (e: Throwable) {
                emit(ViewState.Error(getApiErrorMessage(e)))
            }
        }

    override suspend fun doLogin(requestLogin: RequestLogin): Flow<ViewState<DataLogin>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.doLogin(
                    requestLogin = requestLogin
                )
                val dataLogin = result.data
                if (dataLogin != null) {
                    dataStoreManager.saveLoginStatus(
                        LoginModel(
                            isLogin = true,
                            userName = dataLogin.userName,
                            userImage = dataLogin.userImage,
                            accessToken = dataLogin.accessToken,
                            refreshToken = dataLogin.refreshToken
                        )
                    )
                    emit(ViewState.Success(dataLogin))
                } else {
                    throw Exception("Data login not found")
                }
            } catch (e: Throwable) {
                emit(ViewState.Error(getApiErrorMessage(e)))
            }
        }

}