package com.agungtriu.ecommerce.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {
    const val alfaSize = 0.5f
    const val warningStock = 10

    fun closeSoftKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun displayPrice(basePrice: Int?, variantPrice: Int?): Int {
        return (basePrice ?: 0) + (variantPrice ?: 0)
    }
}
