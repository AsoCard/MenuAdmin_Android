package com.aso.asomenuadmin.model

data class RecipeResponse(
    val result: Recipe,
    val message: String
)

data class Recipe(
    val id: Long,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val img: String?,
    val video: String?,
    val product: Long
)

sealed class ApiState<out T> {
    object Idle : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Failure(val errorMessage: String) : ApiState<Nothing>()
}
