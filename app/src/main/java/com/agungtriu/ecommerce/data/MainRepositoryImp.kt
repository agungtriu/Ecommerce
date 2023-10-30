package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.helper.Utils.getApiErrorMessage
import com.agungtriu.ecommerce.helper.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImp @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
) : MainRepository {
    override suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel) {
        return dataStoreManager.updateLoginStatus(registerProfileModel = registerProfileModel)
    }

    override suspend fun refreshToken(refreshTokenModel: TokenModel) {
        return dataStoreManager.refreshToken(refreshTokenModel = refreshTokenModel)
    }

    override suspend fun changeTheme(isDark: Boolean) {
        dataStoreManager.changeTheme(isDark = isDark)
    }

    override suspend fun changeLang(language: String) {
        dataStoreManager.changeLanguage(language = language)
    }

    override fun getThemeLang(): Flow<ThemeLangModel> {
        return dataStoreManager.getThemeLang()
    }

    override suspend fun deleteLoginStatus() {
        return dataStoreManager.deleteLoginStatus()
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
            } catch (e: Throwable) {
                emit(ViewState.Error(getApiErrorMessage(e)))
            }
        }
}