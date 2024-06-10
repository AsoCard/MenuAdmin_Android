package com.aso.asomenuadmin.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class RecipeState(
    val title: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val imageUrl: String = "",
    val videoUrl: String? = null
)

sealed class RecipeIntent {
    data class LoadRecipe(val productId: Long) : RecipeIntent()
}

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeState())
    val state: StateFlow<RecipeState> get() = _state

    fun handleIntent(intent: RecipeIntent) {
        when (intent) {
            is RecipeIntent.LoadRecipe -> loadRecipe(intent.productId)
        }
    }

    private fun loadRecipe(productId: Long) {
        viewModelScope.launch {
            repository.getRecipe(productId)
                .catch { e ->
                    Timber.e(e, "Error loading recipe")
                    // Emit a failure state or handle the error accordingly
                    // Example of setting an error state:
                    _state.value = RecipeState(
                        title = "Error",
                        ingredients = emptyList(),
                        steps = emptyList(),
                        imageUrl = "",
                        videoUrl = null
                    )
                }
                .collect { apiState ->
                    when (apiState) {
                        is ApiState.Success -> {
                            val result = apiState.data
                            _state.value = RecipeState(
                                title = result.title ?: "",
                                ingredients = result.ingredients?.split(",")?.map { it.trim() } ?: emptyList(),
                                steps = result.steps?.split(",")?.map { it.trim() } ?: emptyList(),
                                imageUrl = result.img ?: "",
                                videoUrl = result.video
                            )
                            Timber.d("Recipe: $result")
                        }
                        is ApiState.Failure -> {
                            Timber.i(apiState.message)
                            _state.value = RecipeState(
                                title = "Error",
                                ingredients = emptyList(),
                                steps = emptyList(),
                                imageUrl = "",
                                videoUrl = null
                            )
                        }
                        ApiState.Loading -> Timber.i("Loading")
                        ApiState.Idle -> Unit // Handle idle state if needed
                    }
                }
        }
    }
}
