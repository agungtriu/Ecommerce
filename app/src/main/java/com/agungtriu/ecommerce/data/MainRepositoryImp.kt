package com.agungtriu.ecommerce.data

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.ViewState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImp @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
) : MainRepository {
    override suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel) {
        dataStoreManager.updateLoginStatus(registerProfileModel = registerProfileModel)
    }

    override suspend fun setRefreshToken(refreshTokenModel: TokenModel) {
        dataStoreManager.setRefreshToken(refreshTokenModel = refreshTokenModel)
    }

    override suspend fun setTheme(isDark: Boolean) {
        dataStoreManager.setTheme(isDark = isDark)
    }

    override suspend fun setLanguage(language: String) {
        dataStoreManager.setLanguage(language = language)
    }

    override fun getThemeLang(): Flow<ThemeLangModel> {
        return dataStoreManager.getThemeLang()
    }

    override fun getAuthorizedStatus(): Flow<AuthorizeModel> {
        return dataStoreManager.getAuthorizedStatus()
    }

    override suspend fun deleteLoginStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.clearAllTables()
        }
        Firebase.messaging.unsubscribeFromTopic("promo")
        dataStoreManager.deleteLoginStatus()
    }

    override suspend fun postProfile(requestProfile: RequestProfile): Flow<ViewState<DataProfile>> =
        flow {
            emit(ViewState.Loading)
            try {
                val result = apiService.postProfile(
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
                }
            } catch (t: Throwable) {
                emit(ViewState.Error(t.toResponseError()))
            }
        }

    override fun selectCountCart(): Flow<Int> = appDatabase.cartDao().selectCountCart()
    override fun selectCountNotification(): Flow<Int> =
        appDatabase.notificationDao().selectCountNotifications()
}
