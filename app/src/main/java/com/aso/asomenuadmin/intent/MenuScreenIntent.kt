package com.aso.asomenuadmin.intent

import com.aso.asomenuadmin.model.Product

sealed class MenuScreenIntent {
    object LoadData : MenuScreenIntent()
    data class DataLoaded(val data: List<Product>) : MenuScreenIntent()
    data class Error(val error: String) : MenuScreenIntent()
}
