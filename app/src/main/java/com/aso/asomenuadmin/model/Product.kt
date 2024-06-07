package com.aso.asomenuadmin.model

data class Product(
    val id: Int,
    val category: String,
    val images: List<Image>,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val detail: String,
    val price: Int,
    val ingredients: String
)

data class Image(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val image: String
)
