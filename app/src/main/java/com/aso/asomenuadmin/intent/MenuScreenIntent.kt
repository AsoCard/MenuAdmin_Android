package com.aso.asomenuadmin.intent

import android.view.Menu
import com.aso.asomenuadmin.model.MenuItem

sealed class MenuScreenIntent {
    object LoadData : MenuScreenIntent()
    data class DataLoaded(val data: List<MenuItem>) : MenuScreenIntent()
    data class Error(val error: String) : MenuScreenIntent()
}
