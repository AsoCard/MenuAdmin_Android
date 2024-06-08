package com.aso.asomenuadmin.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val category: String,
    val images: List<Image>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val name: String,
    val detail: String,
    val price: Double,
    val ingredients: String
)

data class Image(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val image: String
)