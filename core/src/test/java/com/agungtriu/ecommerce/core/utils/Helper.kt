package com.agungtriu.ecommerce.core.utils

import java.io.InputStreamReader

object Helper {
    fun readTestResourceFile(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        val inputStreamReader = InputStreamReader(inputStream)
        return inputStreamReader.readText()
    }
}
