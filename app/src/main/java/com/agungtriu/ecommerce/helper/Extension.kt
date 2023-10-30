package com.agungtriu.ecommerce.helper

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.widget.TextView


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
}