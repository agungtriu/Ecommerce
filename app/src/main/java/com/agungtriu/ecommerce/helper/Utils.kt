package com.agungtriu.ecommerce.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.agungtriu.ecommerce.core.remote.model.response.ResponseError
import com.google.gson.Gson
import retrofit2.HttpException

object Utils {

    fun closeSoftKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setLanguage(languageCode: String) {
        val languageIn: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(languageIn)
    }

    fun getApiErrorMessage(e: Throwable): ResponseError {
        var message = ResponseError(code = null, message = e.message)
        if (e is HttpException) {
            val errorResponse =
                Gson().fromJson(
                    e.response()?.errorBody()?.string(),
                    ResponseError::class.java
                ) ?: ResponseError()
            message = errorResponse
        }
        return message
    }
}