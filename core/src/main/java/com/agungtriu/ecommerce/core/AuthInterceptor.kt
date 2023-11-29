package com.agungtriu.ecommerce.core

import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.utils.Config.API_KEY
import com.agungtriu.ecommerce.core.utils.Config.PRELOGIN_ENDPOINT
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.toString()
        val isPreLoginRequest = PRELOGIN_ENDPOINT.contains(requestUrl)
        val requestWithAuth =
            if (isPreLoginRequest) {
                originalRequest.newBuilder().addHeader("API_KEY", API_KEY)
            } else {
                val token = runBlocking {
                    dataStoreManager.getToken().first()
                }
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${token.accessToken}")
            }

        return chain.proceed(requestWithAuth.build())
    }
}
