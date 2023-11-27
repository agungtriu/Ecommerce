package com.agungtriu.ecommerce.utils

import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.response.DataDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.DataLogin
import com.agungtriu.ecommerce.core.remote.model.response.DataPayment
import com.agungtriu.ecommerce.core.remote.model.response.DataProducts
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataRefresh
import com.agungtriu.ecommerce.core.remote.model.response.DataRegister
import com.agungtriu.ecommerce.core.remote.model.response.DataReview
import com.agungtriu.ecommerce.core.remote.model.response.DataTransaction
import com.agungtriu.ecommerce.core.remote.model.response.DataTransactionProduct
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.core.remote.model.response.Product
import com.agungtriu.ecommerce.core.remote.model.response.ProductVariantItem
import com.agungtriu.ecommerce.core.remote.model.response.ResponseDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.core.remote.model.response.ResponseFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.ResponseLogin
import com.agungtriu.ecommerce.core.remote.model.response.ResponsePayment
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProducts
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProfile
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRating
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRefresh
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRegister
import com.agungtriu.ecommerce.core.remote.model.response.ResponseReviewProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseSearch
import com.agungtriu.ecommerce.core.remote.model.response.ResponseTransaction
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import com.agungtriu.ecommerce.helper.Language

object DataDummy {
    val dummyRegisterResponse = ResponseRegister(
        code = 200,
        message = "OK",
        data = DataRegister(
            userName = "",
            userImage = "",
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDE1MjB9.ldL_6Qoo-MfMmwHrhxXUv670Uz6j0CCF9t9I8uOmW_LuAUTzCWhjMcQelP8MjfnVDqKSZj2LaqHv3TY08AB7TQ",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDQ1MjB9.HeeNuQww-w2tb3pffNC43BCmMCcE3rOj-yL7-pTGOEcIcoFCv2n9IEWS0gqxNnDaNf3sXBm7JHCxFexB5FGRgQ",
            expiresAt = 600
        )
    )

    val dummyRegisterResponseEmailAlreadyRegister = ResponseError(
        code = 400,
        message = "Email is already taken"
    )

    val dummyLoginResponse = ResponseLogin(
        code = 200,
        message = "OK",
        data = DataLogin(
            userName = "",
            userImage = "",
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDE1MjB9.ldL_6Qoo-MfMmwHrhxXUv670Uz6j0CCF9t9I8uOmW_LuAUTzCWhjMcQelP8MjfnVDqKSZj2LaqHv3TY08AB7TQ",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDQ1MjB9.HeeNuQww-w2tb3pffNC43BCmMCcE3rOj-yL7-pTGOEcIcoFCv2n9IEWS0gqxNnDaNf3sXBm7JHCxFexB5FGRgQ",
            expiresAt = 600
        )
    )

    val dummyRefreshResponse = ResponseRefresh(
        code = 200,
        message = "OK",
        data = DataRefresh(
            accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDIwMjN9.g4y-WkXHsk6gTxb72-L2Kk2Wv7dZ438zWZIfJ1Z9bER2Ob3ULnuo2ExBzq5S5l6eJ85PUYOeuiCUCeBRZ94RQQ",
            refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJlY29tbWVyY2UtYXVkaWVuY2UiLCJpc3MiOiJodHRwOi8vMTkyLjE2OC4yMzAuMTI5OjgwODAvIiwidXNlcklkIjoiMzczNTNkMzAtMWIzZC00ZGJlLThmODQtYWZjMjdjNGU5MWJhIiwidHlwZVRva2VuIjoiYWNjZXNzVG9rZW4iLCJleHAiOjE2ODUzNDUwMjN9.U3FQQCGsyBCWE5qUOkWjneI_igtUj9bDKvJI-25o-8a6NMekmvvdlzjJVvK2Yyed9IpAaGTMXNgeQsl9M04uDA",
            expiresAt = 600
        )
    )


    val dummyProfileResponse = ResponseProfile(
        code = 200,
        message = "OK",
        data = DataProfile(
            userName = "Test",
            userImage = "1d32ba79-e879-4425-a011-2da4281f1c1b-test.png"
        )
    )

    val dummyProductsResponse = ResponseProducts(
        code = 200,
        message = "OK",
        data = DataProducts(
            itemsPerPage = 10,
            currentItemCount = 10,
            pageIndex = 1,
            totalPages = 3,
            items = listOf(
                Product(
                    productId = "601bb59a-4170-4b0a-bd96-f34538922c7c",
                    productName = "Lenovo Legion 3",
                    productPrice = 10000000,
                    image = "image1",
                    brand = "Lenovo",
                    store = "LenovoStore",
                    sale = 2,
                    productRating = 4.0
                ),
                Product(
                    productId = "3134a179-dff6-464f-b76e-d7507b06887b",
                    productName = "Lenovo Legion 5",
                    productPrice = 15000000,
                    image = "image1",
                    brand = "Lenovo",
                    store = "LenovoStore",
                    sale = 4,
                    productRating = 4.0
                )
            )
        )
    )

    val dummySearchResponse = ResponseSearch(
        code = 200,
        message = "OK",
        data = listOf(
            "Lenovo Legion 3",
            "Lenovo Legion 5",
            "Lenovo Legion 7",
            "Lenovo Ideapad 3",
            "Lenovo Ideapad 5",
            "Lenovo Ideapad 7"
        )
    )

    val dummyProductByIdResponse = ResponseDetailProduct(
        code = 200,
        message = "OK",
        data = DataDetailProduct(
            productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
            productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
            productPrice = 24499000,
            image = listOf(
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/0cc3d06c-b09d-4294-8c3f-1c37e60631a6.jpg",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/3/25/33a06657-9f88-4108-8676-7adafaa94921.jpg"
            ),
            brand = "Asus",
            description = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray [AMD Ryzen™ 7 6800H / NVIDIA® GeForce RTX™ 3060 / 8G*2 / 512GB / 17.3inch / WIN11 / OHS]\n\nCPU : AMD Ryzen™ 7 6800H Mobile Processor (8-core/16-thread, 20MB cache, up to 4.7 GHz max boost)\nGPU : NVIDIA® GeForce RTX™ 3060 Laptop GPU\nGraphics Memory : 6GB GDDR6\nDiscrete/Optimus : MUX Switch + Optimus\nTGP ROG Boost : 1752MHz* at 140W (1702MHz Boost Clock+50MHz OC, 115W+25W Dynamic Boost)\nPanel : 17.3-inch FHD (1920 x 1080) 16:9 360Hz IPS-level 300nits sRGB % 100.00%",
            store = "AsusStore",
            sale = 12,
            stock = 2,
            totalRating = 7,
            totalReview = 5,
            totalSatisfaction = 100,
            productRating = 5.0F,
            productVariant = listOf(
                ProductVariantItem(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                ),
                ProductVariantItem(
                    variantName = "RAM 32GB",
                    variantPrice = 1000000
                )
            )
        )
    )

    val dummyReviewByProductId = ResponseReviewProduct(
        code = 200,
        message = "OK",
        data = listOf(
            DataReview(
                userName = "John",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
                userRating = 4.0F,
                userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            DataReview(
                userName = "Doe",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
                userRating = 5.0F,
                userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        )
    )

    val dummyPaymentsResponse = ResponsePayment(
        code = 200,
        message = "OK",
        data = listOf(
            DataTypePayment(
                title = "Transfer Virtual Account",
                item = listOf(
                    DataPayment(
                        label = "BCA Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
                        status = true
                    ),
                    DataPayment(
                        label = "BNI Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/id/thumb/5/55/BNI_logo.svg/1200px-BNI_logo.svg.png",
                        status = true
                    ),
                    DataPayment(
                        label = "BRI Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/BANK_BRI_logo.svg/1200px-BANK_BRI_logo.svg.png",
                        status = true
                    ),
                    DataPayment(
                        label = "Mandiri Virtual Account",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Bank_Mandiri_logo_2016.svg/2560px-Bank_Mandiri_logo_2016.svg.png",
                        status = true
                    ),
                )
            ), DataTypePayment(
                title = "Transfer Bank",
                item = listOf(
                    DataPayment(
                        label = "Bank BCA",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Bank_Central_Asia.svg/2560px-Bank_Central_Asia.svg.png",
                        status = true
                    ),
                    DataPayment(
                        label = "Bank BNI",
                        image = "https://upload.wikimedia.org/wikipedia/id/thumb/5/55/BNI_logo.svg/1200px-BNI_logo.svg.png",
                        status = true
                    ),
                    DataPayment(
                        label = "Bank BRI",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/BANK_BRI_logo.svg/1200px-BANK_BRI_logo.svg.png",
                        status = true

                    ),
                    DataPayment(
                        label = "Bank Mandiri",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Bank_Mandiri_logo_2016.svg/2560px-Bank_Mandiri_logo_2016.svg.png",
                        status = true
                    )
                )
            ), DataTypePayment(
                title = "Pembayaran Instan",
                item = listOf(
                    DataPayment(
                        label = "GoPay",
                        image = "https://gopay.co.id/icon.png",
                        status = true

                    ), DataPayment(
                        label = "OVO",
                        image = "https://theme.zdassets.com/theme_assets/1379487/2cb35fe96fa1191f49c2b769b50cf8b546fff65e.png",
                        status = true
                    )
                )
            )
        )
    )

    val dummyFulfillmentResponse = ResponseFulfillment(
        code = 200,
        message = "OK",
        data = DataFulfillment(
            invoiceId = "ba47402c-d263-49d3-a1f8-759ae59fa4a1",
            status = true,
            date = "09 Jun 2023",
            time = "08:53",
            payment = "Bank BCA",
            total = 48998000
        )
    )

    val dummyFulfillmentErrorResponse = ResponseError(
        code = 400,
        message = "Payment or Items must be filled correctly"
    )

    val dummyRating = ResponseRating(
        code = 200,
        message = "Fulfillment rating and review success",
    )

    val dummyTransactionResponse = ResponseTransaction(
        code = 200,
        message = "OK",
        data = listOf(
            DataTransaction(
                invoiceId = "8cad85b1-a28f-42d8-9479-72ce4b7f3c7d",
                status = true,
                date = "09 Jun 2023",
                time = "09:05",
                payment = "Bank BCA",
                total = 48998000,
                items = listOf(
                    DataTransactionProduct(
                        productId = "bee98108-660c-4ac0-97d3-63cdc1492f53",
                        variantName = "RAM 16GB",
                        quantity = 2
                    )
                ),
                rating = 4,
                review = "LGTM",
                image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                name = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray"
            )
        )
    )

    val dummyCartEntity = CartEntity(
        id = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
        productPrice = 24499000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        brand = "Asus",
        store = "AsusStore",
        stock = 2,
        variantName = "RAM 16GB",
        variantPrice = 24499000,
        quantity = 1,
        isSelected = false,
    )

    val dummyUpdateCartEntity = CartEntity(
        id = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
        productPrice = 24499000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        brand = "Asus",
        store = "AsusStore",
        stock = 2,
        variantName = "RAM 16GB",
        variantPrice = 24499000,
        quantity = 2,
        isSelected = true,
    )

    val dummyNotificationEntity = NotificationEntity(
        id = 1,
        title = "Telkomsel Award 2023",
        body = "Nikmati kemeriahan ulang tahun Telkomsel pada hari Jumat, 21 Juli 2023 pukul 19.00 – 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        image = "https://www.telkomsel.com/sites/default/files/banner/hero-10/2023-07/telkomsel-awards-2023--HERO3.png",
        type = "Promo",
        date = "21 Jul 2023",
        time = "12:34",
        isRead = false
    )

    val dummyUpdateNotificationEntity = NotificationEntity(
        id = 1,
        title = "Telkomsel Award 2023",
        body = "Nikmati kemeriahan ulang tahun Telkomsel pada hari Jumat, 21 Juli 2023 pukul 19.00 – 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        image = "https://www.telkomsel.com/sites/default/files/banner/hero-10/2023-07/telkomsel-awards-2023--HERO3.png",
        type = "Promo",
        date = "21 Jul 2023",
        time = "12:34",
        isRead = true
    )


    val dummyWishlistEntity = WishlistEntity(
        id = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
        productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
        productPrice = 24499000,
        image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
        brand = "Asus",
        store = "AsusStore",
        stock = 2,
        variantName = "RAM 16GB",
        variantPrice = 24499000,
        productRating = 5.0F,
        sale = 12,
    )

    val dummyLoginModel = LoginModel(
        isLogin = true,
        userName = dummyLoginResponse.data?.userName,
        userImage = dummyLoginResponse.data?.userImage,
        accessToken = dummyLoginResponse.data?.accessToken,
        refreshToken = dummyLoginResponse.data?.refreshToken,
        isAuthorized = true
    )

    val dummyAuthorizeModel = AuthorizeModel(
        isAuthorized = true
    )

    val dummyThemeLangModel = ThemeLangModel(
        isDark = true,
        language = Language.en.name
    )

    val dummyTokenModel = TokenModel(
        accessToken = dummyLoginResponse.data?.accessToken!!,
        refreshToken = dummyLoginResponse.data?.refreshToken!!,
    )

    val dummyRequestRating = RequestRating(
        invoiceId = "",
        rating = 5,
        review = ""
    )

    val dummyError404Response = ResponseError(
        code = 404,
        message = "Not Found"
    )

    val dummyError403Response = ResponseError(
        code = 403,
        message = "Api key is not valid"
    )
    val dummyError401Response = ResponseError(
        code = 401,
        message = "Token is not valid or has expired"
    )

    val dummyError400Response = ResponseError(
        code = 400,
        message = "Email or password is not valid"
    )
}