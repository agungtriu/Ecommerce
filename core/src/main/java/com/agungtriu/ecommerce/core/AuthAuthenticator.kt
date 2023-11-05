package com.agungtriu.ecommerce.core

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.remote.model.request.RequestRefresh
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRefresh
import com.agungtriu.ecommerce.core.utils.Config.API_BASE_URL
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val chuckerInterceptor: ChuckerInterceptor,
    private val authInterceptor: AuthInterceptor
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            return runBlocking {
                try {
                    val token = dataStoreManager.getToken().first()
                    val refresh = refreshToken(token.refreshToken)
                    if (refresh.data != null) {
                        dataStoreManager.refreshToken(
                            TokenModel(
                                accessToken = refresh.data!!.accessToken!!,
                                refreshToken = refresh.data!!.refreshToken!!
                            )
                        )
                        return@runBlocking response.request.newBuilder()
                            .header(
                                "Authorization",
                                "Bearer ${refresh.data!!.accessToken!!}"
                            )
                            .build()
                    } else {
                        throw Exception()
                    }
                } catch (e: Exception) {
                    dataStoreManager.deleteLoginStatus()
                    return@runBlocking null
                }
            }
        }
    }

    private suspend fun refreshToken(
        token: String
    ): ResponseRefresh {
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_BASE_URL)
            .client(client)
            .build()
        val service = retrofit.create(ApiService::class.java)
        return service.doRefreshToken(RequestRefresh(token = token))
    }
}