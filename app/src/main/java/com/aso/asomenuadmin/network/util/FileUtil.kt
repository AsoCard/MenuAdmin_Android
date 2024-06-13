package com.aso.asomenuadmin.network.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


object FileUtil {
    fun copyUriToFile(context: Context, uri: Uri, file: File) {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream: FileOutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int

        while (inputStream?.read(buffer).also { length = it!! }!! > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream?.close()
    }
}