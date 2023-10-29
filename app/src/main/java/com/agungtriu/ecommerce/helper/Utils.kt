package com.agungtriu.ecommerce.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

object Utils {
    fun getColorAttribute(context: Context, attrResId: Int): Int {
        val typedValue = android.util.TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attrResId, typedValue, true)
        return ContextCompat.getColor(context, typedValue.resourceId)
    }

    fun closeSoftKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}