package com.agungtriu.ecommerce.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.utils.Config.DATASTORE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val dataStore = appContext.dataStore

    fun getOnboardingStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ONBOARDING_KEY] ?: false
        }
    }

    suspend fun saveOnboardingStatus() {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = true
        }
    }

    fun getLoginStatus(): Flow<LoginModel> {
        return dataStore.data.map { preferences ->
            LoginModel(
                isLogin = preferences[LOGIN_KEY] ?: false,
                userName = preferences[NAME_KEY] ?: "",
                userImage = preferences[IMAGE_KEY] ?: "",
                accessToken = preferences[ACCESS_TOKEN_KEY] ?: "",
                refreshToken = preferences[REFRESH_TOKEN_KEY] ?: "",
            )
        }
    }

    fun getToken(): Flow<TokenModel> {
        return dataStore.data.map { preferences ->
            TokenModel(
                accessToken = preferences[ACCESS_TOKEN_KEY] ?: "",
                refreshToken = preferences[REFRESH_TOKEN_KEY] ?: "",
            )
        }
    }

    suspend fun saveLoginStatus(loginModel: LoginModel) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = loginModel.isLogin
            preferences[NAME_KEY] = loginModel.userName ?: ""
            preferences[IMAGE_KEY] = loginModel.userImage ?: ""
            preferences[ACCESS_TOKEN_KEY] = loginModel.accessToken ?: ""
            preferences[REFRESH_TOKEN_KEY] = loginModel.refreshToken ?: ""
        }
    }

    suspend fun refreshToken(refreshTokenModel: TokenModel) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = refreshTokenModel.accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshTokenModel.refreshToken
        }
    }

    suspend fun updateLoginStatus(registerProfileModel: RegisterProfileModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = registerProfileModel.userName ?: ""
            preferences[IMAGE_KEY] = registerProfileModel.userImage ?: ""
        }
    }

    suspend fun deleteLoginStatus() {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = false
            preferences[NAME_KEY] = ""
            preferences[IMAGE_KEY] = ""
            preferences[ACCESS_TOKEN_KEY] = ""
            preferences[REFRESH_TOKEN_KEY] = ""
        }
    }

    companion object {
        private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_status")
        private val LOGIN_KEY = booleanPreferencesKey("login_status")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val IMAGE_KEY = stringPreferencesKey("image")
    }
}