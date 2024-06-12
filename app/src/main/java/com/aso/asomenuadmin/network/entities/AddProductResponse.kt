package com.aso.asomenuadmin.network.entities

import com.google.gson.annotations.SerializedName

data class AddProductResponse (
    @SerializedName("result") val result: ProductResponse,
    @SerializedName("message")
    val message: String
)

data class ProductResponse(
    val id: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val name: String,
    val detail: String,
    val price: Double,
    val ingredients: String,
    val category: Int,
    val images: List<Int>
)