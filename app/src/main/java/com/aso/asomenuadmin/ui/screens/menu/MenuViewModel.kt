package com.aso.asomenuadmin.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.model.Category
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class MenuState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MenuIntent {
    data class LoadProducts(val search: String) : MenuIntent()
    object LoadCategories : MenuIntent()
}

@HiltViewModel
class MenuViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    private val _state = MutableStateFlow(MenuState())
    val state: StateFlow<MenuState> get() = _state

    init {
        handleIntent(MenuIntent.LoadProducts(""))
        handleIntent(MenuIntent.LoadCategories)
    }

    fun handleIntent(intent: MenuIntent) {
        when (intent) {
            is MenuIntent.LoadProducts -> loadProducts(intent.search)
            is MenuIntent.LoadCategories -> loadCategories()
        }
    }

    private fun loadProducts(search: String) {
        viewModelScope.launch {
            repository.getProducts(search)
                .onStart { _state.value = _state.value.copy(isLoading = true) }
                .catch { e -> _state.value = _state.value.copy(isLoading = false, error = e.message) }
                .collect { products ->
                    Timber.d("Products: $products")
                    when (products) {
                        is ApiState.Success -> {
                            _state.value = _state.value.copy(products = products.data.result, isLoading = false)
                            Timber.d("Products: $products")
                        }
                        is ApiState.Failure -> {
                            _state.value = _state.value.copy(error = products.errorMessage, isLoading = false)
                            Timber.e(products.errorMessage)
                        }
                        is ApiState.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                            Timber.d("Loading...")
                        }
                        ApiState.Idle -> {
                            _state.value = _state.value.copy(products = emptyList(), isLoading = false)
                            Timber.d("Idle")
                        }
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
//            repository.getCategories()
//                .onStart { _state.value = _state.value.copy(isLoading = true) }
//                .catch { e -> _state.value = _state.value.copy(isLoading = false, error = e.message) }
//                .collect { categories ->
//                    Timber.d("Categories: $categories")
//                    when (categories) {
//                        is ApiState.Success -> {
//                            _state.value = _state.value.copy(categories = categories.data, isLoading = false)
//                            Timber.d("Categories: $categories")
//                        }
//                        is ApiState.Failure -> {
//                            _state.value = _state.value.copy(error = categories.errorMessage, isLoading = false)
//                            Timber.e(categories.errorMessage)
//                        }
//                        is ApiState.Loading -> {
//                            _state.value = _state.value.copy(isLoading = true)
//                            Timber.d("Loading...")
//                        }
//                        ApiState.Idle -> {
//                            _state.value = _state.value.copy(categories = emptyList(), isLoading = false)
//                            Timber.d("Idle")
//                        }
//                    }
//                }
        }
    }
}
