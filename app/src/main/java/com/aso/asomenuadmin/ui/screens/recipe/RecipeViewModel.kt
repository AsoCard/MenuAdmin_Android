package com.aso.asomenuadmin.ui.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    data class LoadRecipe(val productId: Int) : RecipeIntent()
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

    private fun loadRecipe(productId: Int) {
        viewModelScope.launch {
            val recipe = repository.getRecipe(productId).collect {
                when (it) {
                    is ApiState.Success -> {
                        _state.value = RecipeState(
                            title = it.data.title,
                            ingredients = it.data.ingredients,
                            steps = it.data.steps,
                            imageUrl = it.data.imageUrl,
                            videoUrl = it.data.videoUrl
                        )
                    }

                    is ApiState.Failure -> {
                        Timber.i(
                            it.errorMessage
                        )
                    }

                    ApiState.Idle -> TODO()
                    ApiState.Loading -> Timber.i(
                        "Loading"
                    )
                }

            }

        }
    }
}
