package com.aso.asomenuadmin.repository

import com.aso.asomenuadmin.model.OrderResponse
import com.aso.asomenuadmin.model.Recipe
import com.aso.asomenuadmin.network.ApiService
import com.aso.asomenuadmin.network.apiRequestFlow
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.LoginRequest
import com.aso.asomenuadmin.network.entities.LoginResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Repository {
    //    suspend fun fetchData(): List<Product>
    fun login(email: String, password: String): Flow<ApiState<LoginResponse>>
    fun getRecipe(productId: Long): Flow<ApiState<Recipe>>
    suspend fun getOrders(orderStatus: Int): Flow<ApiState<OrderResponse>>

}

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {
    override fun login(email: String, password: String): Flow<ApiState<LoginResponse>> {
        return apiRequestFlow {
            apiService.login(LoginRequest(email, password))
        }
    }

    override fun getRecipe(productId: Long): Flow<ApiState<Recipe>> {
        return apiRequestFlow {
            apiService.getRecipe(productId)
        }
    }
    override suspend fun getOrders(orderStatus: Int): Flow<ApiState<OrderResponse>> {
        return apiRequestFlow {
            apiService.getOrders(orderStatus)
        }

    }
}
