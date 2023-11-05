package com.agungtriu.ecommerce.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.LoginStatusModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.helper.Extension.toResponseError
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

    override fun getLoginStatus(): Flow<LoginStatusModel> {
        return dataStoreManager.getLoginStatus()
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
            } catch (t: Throwable) {
                emit(ViewState.Error(t.toResponseError()))
            }
        }

    override fun getProducts(requestProducts: RequestProducts): Flow<PagingData<Product>> =
        Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            pagingSourceFactory = {
                ProductsPagingSource(apiService, requestProducts)
            },
        ).flow

    override suspend fun doSearch(query: String): Flow<ViewState<List<String>>> = flow {
        emit(ViewState.Loading)
        try {
            val result = apiService.doSearch(query = query)
            val data = result.data
            if (data != null) {
                emit(ViewState.Success(data))
            } else {
                throw Exception("Data search not found")
            }
        } catch (t: Throwable) {
            emit(ViewState.Error(t.toResponseError()))
        }
    }
}