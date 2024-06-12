package com.aso.asomenuadmin.network.entities

import com.google.gson.annotations.SerializedName

data class AddRecipeRequest(
    val created_at: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val images: List<Int>,
    val video: String?,
    val product: Int
)