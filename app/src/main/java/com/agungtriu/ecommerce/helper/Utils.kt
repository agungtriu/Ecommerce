package com.agungtriu.ecommerce.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object Utils {
    const val rounded = 8
    const val alfaSize = 0.5f
    const val warningStock = 10
    fun closeSoftKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setLanguage(languageCode: String) {
        val languageIn: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(languageIn)
    }
}
