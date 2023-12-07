package com.agungtriu.ecommerce.utils

import com.agungtriu.ecommerce.core.datastore.model.AuthorizeModel
import com.agungtriu.ecommerce.core.datastore.model.LoginModel
import com.agungtriu.ecommerce.core.datastore.model.ThemeLangModel
import com.agungtriu.ecommerce.core.remote.model.request.RequestRating
import com.agungtriu.ecommerce.core.remote.model.response.ResponseDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.core.remote.model.response.ResponseFulfillment
import com.agungtriu.ecommerce.core.remote.model.response.ResponseLogin
import com.agungtriu.ecommerce.core.remote.model.response.ResponsePayment
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProducts
import com.agungtriu.ecommerce.core.remote.model.response.ResponseProfile
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRating
import com.agungtriu.ecommerce.core.remote.model.response.ResponseRegister
import com.agungtriu.ecommerce.core.remote.model.response.ResponseReviewProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseSearch
import com.agungtriu.ecommerce.core.remote.model.response.ResponseTransaction
import com.agungtriu.ecommerce.helper.Language
import com.agungtriu.ecommerce.utils.Extension.toCart
import com.agungtriu.ecommerce.utils.Extension.toNotifications
import com.agungtriu.ecommerce.utils.Extension.toWishlist

object DataDummy {
    val dummyRegisterResponse = Helper.jsonToObject<ResponseRegister>("register.json")

    val dummyLoginResponse = Helper.jsonToObject<ResponseLogin>("login.json")

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

    val dummyNotificationEntity =
        Helper.jsonToObject<ResponseNotification>("notification.json").toNotifications()

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

    val dummyRegisterResponseEmailAlreadyRegister = ResponseError(
        code = 400,
        message = "Email is already taken"
    )

    val dummyFulfillmentErrorResponse = ResponseError(
        code = 400,
        message = "Payment or Items must be filled correctly"
    )

    val dummyAuthorizeModel = AuthorizeModel(
        isAuthorized = true
    )

    val dummyThemeLangModel = ThemeLangModel(
        isDark = true,
        language = Language.EN.name
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

    val dummyError401Response = ResponseError(
        code = 401,
        message = "Token is not valid or has expired"
    )

    val dummyError400Response = ResponseError(
        code = 400,
        message = "Email or password is not valid"
    )
}
