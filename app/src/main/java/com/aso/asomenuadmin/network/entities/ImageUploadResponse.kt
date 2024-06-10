package com.aso.asomenuadmin.network.entities

import com.aso.asomenuadmin.model.Image

data class ImageUploadResponse(
    val result: Image,
    val message: String
)