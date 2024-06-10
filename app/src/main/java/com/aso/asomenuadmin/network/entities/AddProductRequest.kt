package com.aso.asomenuadmin.network.entities

import com.google.gson.annotations.SerializedName


data class AddProductRequest(
    @SerializedName("images") val images: List<Int>,
    @SerializedName("category") val category: Int,
    @SerializedName("name") val name: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("ingredients") val ingredients: String,
    @SerializedName("price") val price: Double
)


