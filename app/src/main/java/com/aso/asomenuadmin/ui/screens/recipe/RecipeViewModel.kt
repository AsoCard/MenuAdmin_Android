package com.aso.asomenuadmin.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeState(
    val title: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val imageUrl: String = "",
    val videoUrl: String? = null
)

sealed class RecipeIntent {
    data class LoadRecipe(val productId: Int) : RecipeIntent()
}

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeState())
    val state: StateFlow<RecipeState> get() = _state

    fun handleIntent(intent: RecipeIntent) {
        when (intent) {
            is RecipeIntent.LoadRecipe -> loadRecipe(intent.productId)
        }
    }

    private fun loadRecipe(productId: Int) {
        viewModelScope.launch {
            val recipe = repository.getRecipe(productId)
            _state.value = RecipeState(
                title = recipe.title,
                ingredients = recipe.ingredients,
                steps = recipe.steps,
                imageUrl = recipe.imageUrl,
                videoUrl = recipe.videoUrl
            )
        }
    }
}
