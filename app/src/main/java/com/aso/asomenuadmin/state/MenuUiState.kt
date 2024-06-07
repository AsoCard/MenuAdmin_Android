package com.aso.asomenuadmin.state

import com.aso.asomenuadmin.model.Product

data class MenuUiState(
    val isLoading: Boolean = false,
    val data: List<Product> = emptyList(),
    val error: String? = null,
)