package com.aso.asomenuadmin.network

data class AddRecipeRequest(
    val created_at: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val img: String?,
    val video: String?,
    val product: Long
)