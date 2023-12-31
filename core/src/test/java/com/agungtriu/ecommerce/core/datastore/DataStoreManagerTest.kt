package com.agungtriu.ecommerce.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.agungtriu.ecommerce.core.DataDummy
import com.agungtriu.ecommerce.core.datastore.model.RegisterProfileModel
import com.agungtriu.ecommerce.core.utils.Config.DATASTORE_NAME
import com.agungtriu.ecommerce.core.utils.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dataStore = PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile(
                DATASTORE_NAME
            )
        })
        dataStoreManager = DataStoreManager(dataStore)
    }

    @Test
    fun setAndGetOnboardingStatus() = runTest {
        dataStoreManager.setOnboardingStatus()
        val actual = dataStoreManager.getOnboardingStatus().first()
        Assert.assertEquals(true, actual)
    }

    @Test
    fun setLoginData_getLoginStatus_getLoginData_getToken_getAuthorized() = runTest {
        dataStoreManager.setLoginData(loginModel = DataDummy.dummyLoginModel)

        val actualLoginStatus = dataStoreManager.getLoginStatus().first()
        Assert.assertEquals(DataDummy.dummyLoginModel.isLogin, actualLoginStatus)

        val actualLoginData = dataStoreManager.getLoginData().first()
        Assert.assertEquals(DataDummy.dummyLoginModel, actualLoginData)

        val actualToken = dataStoreManager.getToken().first()
        Assert.assertEquals(DataDummy.dummyLoginModel.accessToken, actualToken.accessToken)
        Assert.assertEquals(DataDummy.dummyLoginModel.refreshToken, actualToken.refreshToken)

        val actualAuthorized = dataStoreManager.getAuthorizedStatus().first()
        Assert.assertEquals(true, actualAuthorized.isAuthorized)
    }

    @Test
    fun setRefreshToken_getToken() = runTest {
        dataStoreManager.setRefreshToken(refreshTokenModel = DataDummy.dummyTokenModel)
        val actual = dataStoreManager.getToken().first()
        Assert.assertEquals(DataDummy.dummyTokenModel.accessToken, actual.accessToken)
        Assert.assertEquals(DataDummy.dummyTokenModel.refreshToken, actual.refreshToken)
    }

    @Test
    fun setLanguage_setTheme_getThemeLang() = runTest {
        dataStoreManager.setLanguage(language = Language.EN.name)
        dataStoreManager.setTheme(isDark = true)
        val actual = dataStoreManager.getThemeLang().first()
        Assert.assertEquals(Language.EN.name, actual.language)
        Assert.assertEquals(true, actual.isDark)
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
        Assert.assertEquals(username, actual.userName)
        Assert.assertEquals(userImage, actual.userImage)
    }

    @Test
    fun setLoginData_deleteLoginStatus_getLoginData() = runTest {
        dataStoreManager.setLoginData(loginModel = DataDummy.dummyLoginModel)
        dataStoreManager.deleteLoginStatus()
        val actual = dataStoreManager.getLoginData().first()
        Assert.assertEquals(false, actual.isLogin)
        Assert.assertEquals("", actual.userName)
        Assert.assertEquals("", actual.userImage)
        Assert.assertEquals("", actual.accessToken)
        Assert.assertEquals("", actual.refreshToken)
        Assert.assertEquals(false, actual.isAuthorized)
    }
}
