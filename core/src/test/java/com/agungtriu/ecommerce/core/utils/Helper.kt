package com.agungtriu.ecommerce.core.utils

import com.google.gson.Gson
import java.io.InputStreamReader

object Helper {
    fun readTestResourceFile(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        val inputStreamReader = InputStreamReader(inputStream)
        return inputStreamReader.readText()
    }

    inline fun <reified T> jsonToObject(fileName: String): T {
        val response = readTestResourceFile(fileName)
        return Gson().fromJson(response, T::class.java)
    }
}
