package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.remote.model.request.ResponseError
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.helper.ViewState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImp @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
) : Repository {
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

    override suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel) {
        return dataStoreManager.updateLoginStatus(registerProfileModel = registerProfileModel)
    }

    override suspend fun refreshToken(refreshTokenModel: TokenModel) {
        return dataStoreManager.refreshToken(refreshTokenModel = refreshTokenModel)
    }

    override suspend fun deleteLoginStatus() {
        return dataStoreManager.deleteLoginStatus()
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
            } catch (e: Exception) {
                val message = getApiErrorMessage(e)
                emit(ViewState.Error(message.toString()))
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
            } catch (e: Exception) {
                val message = getApiErrorMessage(e)
                emit(ViewState.Error(message.toString()))
            }
        }

    override suspend fun registerProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.registerProfile(
                    requestProfile.userName,
                    requestProfile.userImage
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
            } catch (e: Exception) {
                val message = getApiErrorMessage(e)
                emit(ViewState.Error(message.toString()))
            }
        }

    private fun getApiErrorMessage(e: Exception): String? {
        var message = e.message
        if (e is HttpException) {
            val errorResponse =
                Gson().fromJson(
                    e.response()?.errorBody()?.string(),
                    ResponseError::class.java
                ) ?: ResponseError()
            errorResponse.message?.let { message = it }
        }
        return message
    }
}