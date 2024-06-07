package com.aso.asomenuadmin.repository

import com.aso.asomenuadmin.model.Product

interface Repository {
    suspend fun fetchData(): List<Product>
}

//class RepositoryImpl @Inject constructor(
//    private val apiService: ApiService
//) : Repository {
//    override suspend fun fetchData(): List<Product> {
//        val response = apiService.getBartenderOrders()
//        if (response.isSuccessful) {
//            return response.body() ?: emptyList()
//        } else {
//            throw Exception("Failed to load data")
//        }
//    }
//}
