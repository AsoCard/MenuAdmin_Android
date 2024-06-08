package com.aso.asomenuadmin.model


import com.google.gson.annotations.SerializedName

data class OrderResponse(
    val result: List<Order>,
    val message: String
)

data class Order(
    val id: Int,
    val user: User,
    @SerializedName("orders")
    val products: List<Product>, // Changed from products to orders
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val address: String,
    val status: Int, // Changed from String to Int
    val des: String
)

data class User(
    @SerializedName("full_name")
    val fullName: String,
    val email: String
)

