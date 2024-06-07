package com.aso.asomenuadmin.repository

import com.aso.asomenuadmin.model.Recipe
import com.aso.asomenuadmin.network.ApiService
import javax.inject.Inject

interface RecipeRepository {
    suspend fun getRecipe(productId: Int): Recipe
}

class RecipeRepositoryImpl @Inject constructor(
    private val api: ApiService,
) : RecipeRepository {
    override suspend fun getRecipe(productId: Int): Recipe {
        return api.getRecipe(productId)
    }
}