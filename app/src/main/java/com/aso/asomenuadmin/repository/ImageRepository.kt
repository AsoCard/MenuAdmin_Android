package com.aso.asomenuadmin.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun compressImage(imageUri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val file = File(imageUri.path ?: return@withContext null)
            val compressedImageFile = Compressor.compress(context, file)
            BitmapFactory.decodeFile(compressedImageFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
