package com.aso.asomenuadmin.model

data class Order(
    val id: Int,
    val user: User,
    val orders: List<Product>,
    val createdAt: String,
    val updatedAt: String,
    val address: String,
    val status: String,
    val des: String
)

data class User(
    val fullName: String,
    val email: String
)