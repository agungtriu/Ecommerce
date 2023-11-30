package com.agungtriu.ecommerce.helper

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object PhotoUriManager {
    private const val PHOTOS_DIR = "photos"
    private const val FILE_PROVIDER = "file_provider"
    private fun generateFilename() = "${System.currentTimeMillis()}.jpg"

    fun buildNewUri(appContext: Context): Uri {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${appContext.packageName}.$FILE_PROVIDER"
        return FileProvider.getUriForFile(appContext, authority, photoFile)
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_file.jpg")
        file.createNewFile()

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
