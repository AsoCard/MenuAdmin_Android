package com.aso.asomenuadmin.model

data class Recipe(
    val id: Long,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val img: String?,
    val video: String?,
    val product: Long
)

