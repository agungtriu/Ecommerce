package com.agungtriu.ecommerce.utils

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.agungtriu.ecommerce.core.remote.model.response.ResponseDetailProduct
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.core.room.entity.CartEntity
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.core.room.entity.WishlistEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

object Extension {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) = Unit
            override fun onInserted(position: Int, count: Int) = Unit
            override fun onRemoved(position: Int, count: Int) = Unit
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, UnconfinedTestDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                onListPresentable()
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }

    fun ResponseError.toHttpException(): HttpException {
        val errorBody =
            "{code:${this.code}, message:\"${this.message}\"}".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<String>(this.code!!, errorBody)
        return HttpException(errorResponse)
    }

    fun String.toMultipartBodyPart(name: String): MultipartBody.Part {
        val textBody = this.toRequestBody("text/plain".toMediaType())
        return MultipartBody.Part.createFormData(name, null, textBody)
    }

    fun ResponseDetailProduct.toCart(quantity: Int): CartEntity {
        return CartEntity(
            id = this.data?.productId ?: "",
            productName = this.data?.productName,
            image = this.data?.image?.get(0),
            productPrice = this.data?.productPrice,
            brand = this.data?.brand,
            store = this.data?.store,
            stock = this.data?.stock,
            variantPrice = this.data?.productVariant?.get(0)?.variantPrice?.plus(
                this.data?.productPrice ?: 0
            ),
            variantName = this.data?.productName,
            quantity = quantity
        )
    }

    fun ResponseDetailProduct.toWishlist(): WishlistEntity {
        return WishlistEntity(
            id = this.data?.productId ?: "",
            productName = this.data?.productName,
            image = this.data?.image?.get(0),
            productPrice = this.data?.productPrice,
            brand = this.data?.brand,
            store = this.data?.store,
            productRating = this.data?.productRating,
            sale = this.data?.sale,
            stock = this.data?.stock,
            variantPrice = this.data?.productVariant?.get(0)?.variantPrice?.plus(
                this.data?.productPrice ?: 0
            ),
            variantName = this.data?.productName,
        )
    }

    fun ResponseNotification.toNotifications(isRead: Boolean = false): NotificationEntity {
        return NotificationEntity(
            id = 1,
            title = this.message?.data?.title,
            body = this.message?.data?.body,
            image = this.message?.data?.image,
            date = this.message?.data?.date,
            time = this.message?.data?.time,
            type = this.message?.data?.type,
            isRead = isRead
        )
    }
}
