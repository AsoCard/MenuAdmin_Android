package com.aso.asomenuadmin.network

import com.aso.asomenuadmin.model.Menu
import com.aso.asomenuadmin.model.Order
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.model.Recipe
import com.ezcall.data.dataSource.remote.entities.LoginResponse
import com.ezcall.data.dataSource.remote.entities.TokenVerify
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/api/auth/jwt/login/")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @POST("/api/auth/jwt/refresh/")
    suspend fun refreshToken(@Body refreshToken: TokenVerify): LoginResponse

    @GET("/api/menus/")
    fun getMenus(): Flow<List<Menu>>

    @GET("/api/orders/bartender/")
    fun getBartenderOrders(): Flow<List<Order>>

//    @POST("/api/orders/create/")
//    suspend fun createOrder(@Body createOrder: CreateOrder): Order

    @GET("/api/products/")
    fun getProducts(@Query("search") search: String): Flow<List<Product>>

    @GET("/api/products/{id}/")
    fun getProductById(@Path("id") id: Int): Flow<Product>

    @PUT("/api/products/{id}/")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product

    @PATCH("/api/products/{id}/")
    suspend fun patchProduct(@Path("id") id: Int, @Body product: Product): Product

    @DELETE("/api/products/{id}/")
    suspend fun deleteProduct(@Path("id") id: Int): Void

    @GET("/api/recipes/{productId}/")
    suspend fun getRecipe(@Path("productId") productId: Int): Recipe
}
