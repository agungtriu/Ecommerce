package com.agungtriu.ecommerce.core.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.agungtriu.ecommerce.core.DataDummy
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.utils.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [33])
class DataStoreManagerTest {

    private lateinit var context: Context
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dataStoreManager = DataStoreManager(context)
    }

    @Test
    fun setAndGetOnboardingStatus() = runTest {
        dataStoreManager.setOnboardingStatus()
        val actual = dataStoreManager.getOnboardingStatus().first()
        assertEquals(true, actual)
    }

    @Test
    fun setLoginData_getLoginData_getToken_getAuthorized() = runTest {
        dataStoreManager.setLoginData(loginModel = DataDummy.dummyLoginModel)
        val actual = dataStoreManager.getLoginData().first()
        assertEquals(DataDummy.dummyLoginModel, actual)

        val actualToken = dataStoreManager.getToken().first()
        assertEquals(DataDummy.dummyLoginModel.accessToken, actualToken.accessToken)
        assertEquals(DataDummy.dummyLoginModel.refreshToken, actualToken.refreshToken)

        val actualAuthorized = dataStoreManager.getAuthorizedStatus().first()
        assertEquals(true, actualAuthorized.isAuthorized)
    }

    @Test
    fun setRefreshToken_getToken() = runTest {
        dataStoreManager.setRefreshToken(refreshTokenModel = DataDummy.dummyTokenModel)
        val actual = dataStoreManager.getToken().first()
        assertEquals(DataDummy.dummyTokenModel.accessToken, actual.accessToken)
        assertEquals(DataDummy.dummyTokenModel.refreshToken, actual.refreshToken)
    }

    @Test
    fun setLanguage_setTheme_getThemeLang() = runTest {
        dataStoreManager.setLanguage(language = Language.en.name)
        dataStoreManager.setTheme(isDark = true)
        val actual = dataStoreManager.getThemeLang().first()
        assertEquals(Language.en.name, actual.language)
        assertEquals(true, actual.isDark)
    }

    @Test
    fun setLoginData_updateLoginStatus_getLoginData() = runTest {
        dataStoreManager.setLoginData(loginModel = DataDummy.dummyLoginModel)
        val username = "mamama"
        val userImage = ""
        dataStoreManager.updateLoginStatus(
            registerProfileModel = RegisterProfileModel(
                userName = username,
                userImage = userImage
            )
        )
        val actual = dataStoreManager.getLoginData().first()
        assertEquals(username, actual.userName)
        assertEquals(userImage, actual.userImage)
    }

    @Test
    fun setLoginData_deleteLoginStatus_getLoginData() = runTest {
        dataStoreManager.setLoginData(loginModel = DataDummy.dummyLoginModel)
        dataStoreManager.deleteLoginStatus()
        val actual = dataStoreManager.getLoginData().first()
        assertEquals(false, actual.isLogin)
        assertEquals("", actual.userName)
        assertEquals("", actual.userImage)
        assertEquals("", actual.accessToken)
        assertEquals("", actual.refreshToken)
        assertEquals(false, actual.isAuthorized)
    }
}