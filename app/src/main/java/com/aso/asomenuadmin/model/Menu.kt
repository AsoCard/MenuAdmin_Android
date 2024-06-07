package com.aso.asomenuadmin.model

data class Menu(
    val id: Int,
    val products: List<Product>,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val type: String
)