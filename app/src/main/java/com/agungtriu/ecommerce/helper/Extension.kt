package com.agungtriu.ecommerce.helper

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException


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

    fun Int.toRupiah(): String {
        val stringNumber = this.toString()
        val rest = stringNumber.length % 3
        var rupiah = "Rp. ${stringNumber.slice(0 until rest)}"
        val thousand = stringNumber.slice(rest until stringNumber.length)
        for (index in thousand.indices) {
            rupiah += if (index % 3 == 0) {
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
                ResponseError(code = 503, message = this.message.toString())
            }

            else -> {
                ResponseError(code = null, message = this.message)
            }
        }
    }
}