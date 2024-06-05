package com.aso.asomenuadmin.state

import com.aso.asomenuadmin.model.MenuItem

data class MenuUiState(
    val isLoading: Boolean = false,
    val data: List<MenuItem> = emptyList(),
    val error: String? = null,
)