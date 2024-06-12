package com.aso.asomenuadmin.network.entities

import com.aso.asomenuadmin.model.Image
import com.aso.asomenuadmin.model.Recipe

data class AddRecipeResponse(
    val result: AddRecipe,
    val message: String
)


data class AddRecipe(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val product: Int,
    val video: String?,
    val images: List<Int>
)