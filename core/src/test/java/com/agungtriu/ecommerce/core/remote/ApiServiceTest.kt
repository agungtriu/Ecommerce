package com.agungtriu.ecommerce.core.remote

import com.agungtriu.ecommerce.core.DataDummy
import com.agungtriu.ecommerce.core.remote.model.request.ProductFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestFulfillment
import com.agungtriu.ecommerce.core.remote.model.request.RequestLogin
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.request.RequestRefresh
import com.agungtriu.ecommerce.core.remote.model.request.RequestRegister
import com.agungtriu.ecommerce.core.utils.Helper
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class ApiServiceTest {
    private lateinit var server: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        apiService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun postRegister() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("register.json"))

        server.enqueue(response)

        val actualData = apiService.postRegister(RequestRegister("", "", ""))
        val expectedData = DataDummy.dummyRegisterResponse

        assertEquals(expectedData, actualData)
    }

    @Test
    fun postLogin() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("login.json"))

        server.enqueue(response)

        val actualData = apiService.postLogin(RequestLogin("", "", ""))
        val expectedData = DataDummy.dummyLoginResponse

        assertEquals(expectedData, actualData)
    }

    @Test
    fun postRefreshToken() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("refresh.json"))

        server.enqueue(response)

        val actualData = apiService.postRefreshToken(RequestRefresh(token = ""))
        val expectedData = DataDummy.dummyRefreshResponse

        assertEquals(expectedData, actualData)
    }

    @Test
    fun postProfile() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("profile.json"))

        server.enqueue(response)

        val textBody = "".toRequestBody("text/plain".toMediaType())
        val userNamePart = MultipartBody.Part.createFormData("userName", null, textBody)
        val actualData = apiService.postProfile(userName = userNamePart, userImage = null)
        val expectedData = DataDummy.dummyProfileResponse

        assertEquals(expectedData, actualData)
    }

    @Test
    fun getProducts() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("products.json"))

        server.enqueue(response)

        val actualData = apiService.getProducts()
        val expectedData = DataDummy.dummyProductsResponse
        assertEquals(expectedData, actualData)
    }

    @Test
    fun postSearch() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("search.json"))

        server.enqueue(response)

        val actualData = apiService.postSearch(query = "Lenovo")
        val expectedData = DataDummy.dummySearchResponse
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getProductById() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("productById.json"))

        server.enqueue(response)

        val actualData = apiService.getProductById("")
        val expectedData = DataDummy.dummyProductByIdResponse
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getReviewsByProductId() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("reviewByProductId.json"))

        server.enqueue(response)

        val actualData = apiService.getReviewsByProductId("")
        val expectedData = DataDummy.dummyReviewByProductId
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getPayments() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("payments.json"))

        server.enqueue(response)

        val actualData = apiService.getPayments()
        val expectedData = DataDummy.dummyPaymentsResponse
        assertEquals(expectedData, actualData)
    }

    @Test
    fun postFulfillment() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("fulfillment.json"))

        server.enqueue(response)

        val actualData = apiService.postFulfillment(
            requestFulfillment = RequestFulfillment(
                payment = "",
                items = listOf(
                    ProductFulfillment(productId = "", variantName = "", quantity = 1)
                )
            )
        )
        val expectedData = DataDummy.dummyFulfillmentResponse
        assertEquals(expectedData, actualData)
    }

    @Test
    fun postRating() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("rating.json"))

        server.enqueue(response)

        val actualData = apiService.postRating(
            requestRating = RequestRating(
                invoiceId = "",
                rating = 5,
                review = ""
            )
        )
        val expectedData = DataDummy.dummyRating
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getTransactions() = runTest {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(Helper.readTestResourceFile("transaction.json"))

        server.enqueue(response)

        val actualData = apiService.getTransactions()
        val expectedData = DataDummy.dummyTransactionResponse
        assertEquals(expectedData, actualData)
    }
}
