package com.aso.asomenuadmin.repository

import com.aso.asomenuadmin.model.MenuItem
import com.aso.asomenuadmin.network.ApiService
import javax.inject.Inject

interface Repository {
    suspend fun fetchData(): List<MenuItem>
}

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {
    override suspend fun fetchData(): List<MenuItem> {
        val response = apiService.getData()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to load data")
        }
    }
}
