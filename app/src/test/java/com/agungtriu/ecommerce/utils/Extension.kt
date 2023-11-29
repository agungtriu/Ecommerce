package com.agungtriu.ecommerce.utils

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
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
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
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
}
