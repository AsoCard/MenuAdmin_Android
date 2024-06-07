package com.aso.asomenuadmin.model

data class Recipe(
    val title: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val imageUrl: String,
    val videoUrl: String? = null
)
