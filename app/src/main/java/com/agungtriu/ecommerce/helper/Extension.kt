package com.agungtriu.ecommerce.helper

import android.content.Context
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.agungtriu.ecommerce.core.remote.model.request.RequestProducts
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.agungtriu.ecommerce.helper.Utils.filterCategoriesInt
import com.agungtriu.ecommerce.helper.Utils.filterSortInt
import com.agungtriu.ecommerce.ui.main.store.filter.FilterModel
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

object Extension {

    fun TextView.setColor(subtext: String, color: Int) {
        this.setText(this.text, TextView.BufferType.SPANNABLE)
        val str = this.text as Spannable
        val i = this.text.indexOf(subtext)
        str.setSpan(
            ForegroundColorSpan(color),
            i,
            i + subtext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private const val section = 3
    fun Int.toRupiah(): String {
        val stringNumber = this.toString()
        val rest = stringNumber.length % section
        var rupiah = "Rp${stringNumber.slice(0 until rest)}"
        val thousand = stringNumber.slice(rest until stringNumber.length)
        for (index in thousand.indices) {
            rupiah += if (index % section == 0) {
                if (rest == 0 && index == 0) {
                    "${thousand[index]}"
                } else {
                    ".${thousand[index]}"
                }
            } else {
                "${thousand[index]}"
            }
        }
        return rupiah
    }

    fun Throwable.toResponseError(): ResponseError {
        return when (this) {
            is HttpException -> {
                Gson().fromJson(
                    this.response()?.errorBody()?.string(), ResponseError::class.java
                ) ?: ResponseError()
            }

            is IOException -> {
                ResponseError(
                    code = HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
                    message = this.message.toString()
                )
            }

            else -> {
                ResponseError(code = null, message = this.message)
            }
        }
    }

    fun RequestProducts.toFilterModel(): FilterModel {
        return FilterModel(
            sort = this.sort,
            category = this.brand,
            min = this.lowest,
            max = this.highest
        )
    }
}
