package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.ViewState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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

    override suspend fun setOnboarding() {
        dataStoreManager.setOnboardingStatus()
    }

    override fun getLoginData(): Flow<LoginModel> {
        return dataStoreManager.getLoginData()
    }

    override suspend fun setLoginData(loginModel: LoginModel) {
        dataStoreManager.setLoginData(
            loginModel = loginModel
        )
    }

    override suspend fun postRegister(requestRegister: RequestRegister): Flow<ViewState<DataRegister>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.postRegister(
                    requestRegister = requestRegister
                )
                val dataRegister = result.data
                if (dataRegister != null) {
                    dataStoreManager.setLoginData(
                        LoginModel(
                            isLogin = true,
                            userName = dataRegister.userName,
                            userImage = dataRegister.userImage,
                            accessToken = dataRegister.accessToken,
                            refreshToken = dataRegister.refreshToken,
                            isAuthorized = true
                        )
                    )
                    Firebase.messaging.subscribeToTopic("promo")
                    emit(ViewState.Success(dataRegister))
                } else {
                    throw Exception("Data register not found")
                }
            } catch (t: Throwable) {
                emit(ViewState.Error(t.toResponseError()))
            }
        }

    override suspend fun postLogin(requestLogin: RequestLogin): Flow<ViewState<DataLogin>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.postLogin(
                    requestLogin = requestLogin
                )
                val dataLogin = result.data
                if (dataLogin != null) {
                    dataStoreManager.setLoginData(
                        LoginModel(
                            isLogin = true,
                            userName = dataLogin.userName,
                            userImage = dataLogin.userImage,
                            accessToken = dataLogin.accessToken,
                            refreshToken = dataLogin.refreshToken,
                            isAuthorized = true
                        )
                    )
                    Firebase.messaging.subscribeToTopic("promo")
                    emit(ViewState.Success(dataLogin))
                } else {
                    throw Exception("Data login not found")
                }
            } catch (t: Throwable) {
                emit(ViewState.Error(t.toResponseError()))
            }
        }
}