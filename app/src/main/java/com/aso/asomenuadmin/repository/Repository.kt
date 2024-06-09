package com.aso.asomenuadmin.repository

import android.content.Context
import com.aso.asomenuadmin.model.OrderResponse
import com.aso.asomenuadmin.model.Recipe
import com.aso.asomenuadmin.network.ApiService
import com.aso.asomenuadmin.network.apiRequestFlow
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.LoginRequest
import com.aso.asomenuadmin.network.entities.LoginResponse
import com.aso.asomenuadmin.network.util.NetworkUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface Repository {
    //    suspend fun fetchData(): List<Product>
    fun login(email: String, password: String): Flow<ApiState<LoginResponse>>
    fun getRecipe(productId: Long): Flow<ApiState<Recipe>>
    suspend fun getOrders(orderStatus: Int): Flow<ApiState<OrderResponse>>
    suspend fun updateOrderStatus(orderId: Int, orderStatus: Int): Flow<ApiState<Any>>

}
class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkUtil: NetworkUtil
) : Repository {
    override fun login(email: String, password: String): Flow<ApiState<LoginResponse>> {
        return if (networkUtil.isNetworkConnected()) {
            apiRequestFlow { apiService.login(LoginRequest(email, password)) }
        } else {
            flow { emit(ApiState.Failure("No internet connection", -1)) }
        }
    }

    override fun getRecipe(productId: Long): Flow<ApiState<Recipe>> {
        return if (networkUtil.isNetworkConnected()) {
            apiRequestFlow { apiService.getRecipe(productId) }
        } else {
            flow { emit(ApiState.Failure("No internet connection", -1)) }
        }
    }

    override suspend fun getOrders(orderStatus: Int): Flow<ApiState<OrderResponse>> {
        return if (networkUtil.isNetworkConnected()) {
            apiRequestFlow { apiService.getOrders(orderStatus) }
        } else {
            flow { emit(ApiState.Failure("No internet connection", -1)) }
        }
    }

    override suspend fun updateOrderStatus(orderId: Int, orderStatus: Int): Flow<ApiState<Any>> {
        return if (networkUtil.isNetworkConnected()) {
            apiRequestFlow { apiService.updateOrder(orderId, orderStatus) }
        } else {
            flow { emit(ApiState.Failure("No internet connection", -1)) }
        }

    }
}