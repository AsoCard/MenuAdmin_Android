package com.aso.asomenuadmin.network.entities

import com.aso.asomenuadmin.model.Product
import com.google.gson.annotations.SerializedName

data class AddProductResponse (
    @SerializedName("result")
    val result: AddProductRequest,
    @SerializedName("message")
    val message: String
)