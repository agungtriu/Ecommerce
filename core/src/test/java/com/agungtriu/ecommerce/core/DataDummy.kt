package com.agungtriu.ecommerce.core

import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.TokenModel
import com.agungtriu.ecommerce.core.remote.model.response.ResponseDetailProduct
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
import com.agungtriu.ecommerce.core.utils.Extension.toCart
import com.agungtriu.ecommerce.core.utils.Extension.toNotifications
import com.agungtriu.ecommerce.core.utils.Extension.toWishlist
import com.agungtriu.ecommerce.core.utils.Helper
import com.agungtriu.ecommerce.core.utils.ResponseNotification

object DataDummy {
    val dummyRegisterResponse = Helper.jsonToObject<ResponseRegister>("register.json")

    val dummyLoginResponse = Helper.jsonToObject<ResponseLogin>("login.json")

    val dummyRefreshResponse = Helper.jsonToObject<ResponseRefresh>("refresh.json")

    val dummyProfileResponse = Helper.jsonToObject<ResponseProfile>("profile.json")

    val dummyProductsResponse = Helper.jsonToObject<ResponseProducts>("products.json")

    val dummySearchResponse = Helper.jsonToObject<ResponseSearch>("search.json")

    val dummyProductByIdResponse = Helper.jsonToObject<ResponseDetailProduct>("productById.json")

    val dummyReviewByProductId =
        Helper.jsonToObject<ResponseReviewProduct>("reviewByProductId.json")

    val dummyPaymentsResponse = Helper.jsonToObject<ResponsePayment>("payments.json")

    val dummyFulfillmentResponse = Helper.jsonToObject<ResponseFulfillment>("fulfillment.json")

    val dummyRating = Helper.jsonToObject<ResponseRating>("rating.json")

    val dummyTransactionResponse = Helper.jsonToObject<ResponseTransaction>("transaction.json")

    val dummyCartEntity = Helper.jsonToObject<ResponseDetailProduct>("productById.json").toCart(1)

    val dummyUpdateCartEntity =
        Helper.jsonToObject<ResponseDetailProduct>("productById.json").toCart(2)

    val dummyNotificationEntity =
        Helper.jsonToObject<ResponseNotification>("notification.json").toNotifications()

    val dummyUpdateNotificationEntity =
        Helper.jsonToObject<ResponseNotification>("notification.json").toNotifications(true)

    val dummyWishlistEntity =
        Helper.jsonToObject<ResponseDetailProduct>("productById.json").toWishlist()

    val dummyLoginModel = LoginModel(
        isLogin = true,
        userName = dummyLoginResponse.data?.userName,
        userImage = dummyLoginResponse.data?.userImage,
        accessToken = dummyLoginResponse.data?.accessToken,
        refreshToken = dummyLoginResponse.data?.refreshToken,
        isAuthorized = true
    )

    val dummyTokenModel = TokenModel(
        accessToken = dummyLoginResponse.data?.accessToken!!,
        refreshToken = dummyLoginResponse.data?.refreshToken!!,
    )
}
