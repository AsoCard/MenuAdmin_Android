package com.aso.asomenuadmin.network.entities

import com.aso.asomenuadmin.model.Recipe

data class AddRecipeResponse(
    val result: Recipe,
    val message: String
)


