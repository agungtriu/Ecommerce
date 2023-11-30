package com.agungtriu.ecommerce.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.data.MainRepository
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.utils.DataDummy
import com.agungtriu.ecommerce.utils.Extension.toMultipartBodyPart
import com.agungtriu.ecommerce.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mainRepository: MainRepository

    @Before
    fun setUp() {
        mainRepository = mock()
        profileViewModel = ProfileViewModel(mainRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postProfile_success() = runTest {
        val userNamePart = "".toMultipartBodyPart("userName")
        whenever(
            mainRepository.postProfile(
                requestProfile = RequestProfile(
                    userNamePart,
                    null
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProfileResponse.data!!)
            )
        )

        profileViewModel.postProfile(
            requestProfile = RequestProfile(
                userNamePart,
                null
            )
        )
        val actual = mutableListOf<ViewState<DataProfile>>()
        profileViewModel.resultRegisterProfile.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Success(DataDummy.dummyProfileResponse.data!!)
            ),
            actual
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun postProfile_error() = runTest {
        val userNamePart = "".toMultipartBodyPart("userName")
        whenever(
            mainRepository.postProfile(
                requestProfile = RequestProfile(
                    userNamePart,
                    null
                )
            )
        ).thenReturn(
            flowOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            )
        )

        profileViewModel.postProfile(
            requestProfile = RequestProfile(
                userNamePart,
                null
            )
        )
        val actual = mutableListOf<ViewState<DataProfile>>()
        profileViewModel.resultRegisterProfile.observeForever {
            actual.add(it)
        }
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                ViewState.Loading,
                ViewState.Error(DataDummy.dummyError401Response)
            ),
            actual
        )
    }
}
