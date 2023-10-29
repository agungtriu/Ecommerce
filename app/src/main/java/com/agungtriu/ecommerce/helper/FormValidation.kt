package com.agungtriu.ecommerce.helper

object FormValidation {
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(text: String): Boolean {
        return text.length >= 8
    }

}