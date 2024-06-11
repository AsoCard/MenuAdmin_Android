package com.aso.asomenuadmin.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.model.Category
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state

    private val _intent = MutableSharedFlow<MenuIntent>()
    private val intent: SharedFlow<MenuIntent> = _intent

    init {
        handleIntents()
        sendIntent(MenuIntent.LoadProducts)
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collect { menuIntent ->
                when (menuIntent) {
                    is MenuIntent.LoadProducts -> loadProducts()
                    is MenuIntent.DeleteProduct -> deleteProduct(menuIntent.productId)
                    is MenuIntent.EditProduct -> editProduct(menuIntent.product)
                }
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getProducts().collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> {
                        _state.value = _state.value.copy(
                            products = apiState.data.result,
                            isLoading = false,
                            error = null
                        )
                    }
                    is ApiState.Failure -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = apiState.message
                        )
                    }

                    ApiState.Idle -> {
                        Timber.d("Products Idle")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    ApiState.Loading -> {
                        Timber.d("Products Loading")
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            repository.deleteProduct(productId).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> loadProducts()
                    is ApiState.Failure -> loadProducts() // fix this response success is 204 but we get it as error
//                    is ApiState.Failure -> _state.value = _state.value.copy(error = apiState.message)
                    ApiState.Idle -> Timber.d("Delete product Idle")
                    ApiState.Loading -> Timber.d("Delete product Loading")
                }
            }
        }
    }

    private fun editProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product.id, product).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> loadProducts()
                    is ApiState.Failure -> _state.value = _state.value.copy(error = apiState.message)
                    ApiState.Idle -> Timber.d("Edit product Idle")
                    ApiState.Loading -> Timber.d("Edit product Loading")
                }
            }
        }
    }

    fun sendIntent(menuIntent: MenuIntent) {
        viewModelScope.launch {
            _intent.emit(menuIntent)
        }
    }
}

sealed class MenuIntent {
    object LoadProducts : MenuIntent()
    data class DeleteProduct(val productId: Int) : MenuIntent()
    data class EditProduct(val product: Product) : MenuIntent()
}

data class MenuUiState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
