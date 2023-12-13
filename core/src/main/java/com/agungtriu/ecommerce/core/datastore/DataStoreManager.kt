package com.agungtriu.ecommerce.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.utils.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    fun getOnboardingStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ONBOARDING_KEY] ?: false
        }
    }

    suspend fun setOnboardingStatus() {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = true
        }
    }

    fun getLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    fun getLoginData(): Flow<LoginModel> {
        return dataStore.data.map { preferences ->
            LoginModel(
                isLogin = preferences[LOGIN_KEY] ?: false,
                userName = preferences[NAME_KEY] ?: "",
                userImage = preferences[IMAGE_KEY] ?: "",
                accessToken = preferences[ACCESS_TOKEN_KEY] ?: "",
                refreshToken = preferences[REFRESH_TOKEN_KEY] ?: "",
                isAuthorized = preferences[AUTHORIZED_KEY] ?: false,
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

    fun getAuthorizedStatus(): Flow<AuthorizeModel> {
        return dataStore.data.map { preferences ->
            AuthorizeModel(
                isAuthorized = preferences[AUTHORIZED_KEY],
            )
        }
    }

    suspend fun setLoginData(loginModel: LoginModel) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = loginModel.isLogin ?: false
            preferences[NAME_KEY] = loginModel.userName ?: ""
            preferences[IMAGE_KEY] = loginModel.userImage ?: ""
            preferences[ACCESS_TOKEN_KEY] = loginModel.accessToken ?: ""
            preferences[REFRESH_TOKEN_KEY] = loginModel.refreshToken ?: ""
            preferences[AUTHORIZED_KEY] = loginModel.isAuthorized ?: false
        }
    }

    suspend fun setRefreshToken(refreshTokenModel: TokenModel) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = refreshTokenModel.accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshTokenModel.refreshToken
        }
    }

    suspend fun setTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    fun getThemeLang(): Flow<ThemeLangModel> {
        return dataStore.data.map { preferences ->
            ThemeLangModel(
                isDark = preferences[THEME_KEY] ?: false,
                language = preferences[LANGUAGE_KEY] ?: Language.EN.name,
            )
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
            preferences[AUTHORIZED_KEY] = false
        }
    }

    companion object {
        private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_status")
        private val LOGIN_KEY = booleanPreferencesKey("login_status")
        private val AUTHORIZED_KEY = booleanPreferencesKey("authorized_status")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val IMAGE_KEY = stringPreferencesKey("image")
        private val THEME_KEY = booleanPreferencesKey("theme")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
    }
}
