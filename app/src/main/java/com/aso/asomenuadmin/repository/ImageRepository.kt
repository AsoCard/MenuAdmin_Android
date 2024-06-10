package com.aso.asomenuadmin.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.aso.asomenuadmin.network.ApiService
import com.aso.asomenuadmin.network.apiRequestFlow
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.ImageUploadResponse
import com.aso.asomenuadmin.network.util.FileUtil
import com.aso.asomenuadmin.network.util.NetworkUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkUtil: NetworkUtil,
    private val apiService: ApiService,


    ) {

    suspend fun compressImage(imageUri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val file = File(context.cacheDir, "temp_image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()

            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun uploadImage(imageUri: Uri): Flow<ApiState<ImageUploadResponse>> {
        val bitmap = compressImage(imageUri) ?: return flow {
            emit(ApiState.Failure("Image compression failed", -1))
        }

        val file = File(context.cacheDir, "temp_image.jpg")
        FileUtil.copyUriToFile(context, imageUri, file)

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return if (networkUtil.isNetworkConnected()) {
            apiRequestFlow { apiService.uploadImage(body) }
        } else {
            flow { emit(ApiState.Failure("No internet connection", -1)) }
        }
    }
}
