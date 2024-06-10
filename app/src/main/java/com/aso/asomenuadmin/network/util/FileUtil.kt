package com.aso.asomenuadmin.network.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


object FileUtil {
    fun copyUriToFile(context: Context, uri: Uri, file: File): Boolean {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream: OutputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}