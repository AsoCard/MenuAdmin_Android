package com.aso.asomenuadmin.model

data class Recipe(
    val id: Long,
    val images: List<Image>,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val video: String?,
    val product: Int
)

