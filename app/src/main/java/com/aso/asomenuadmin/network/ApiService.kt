package com.aso.asomenuadmin.network

import com.aso.asomenuadmin.model.Menu
import com.aso.asomenuadmin.model.OrderResponse
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.model.ProductResponse
import com.aso.asomenuadmin.network.entities.AddProductRequest
import com.aso.asomenuadmin.network.entities.AddProductResponse
import com.aso.asomenuadmin.network.entities.AddRecipeRequest
import com.aso.asomenuadmin.network.entities.AddRecipeResponse
import com.aso.asomenuadmin.network.entities.GetRecipeResponse
import com.aso.asomenuadmin.network.entities.ImageUploadResponse
import com.aso.asomenuadmin.network.entities.LoginRequest
import com.aso.asomenuadmin.network.entities.LoginResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/jwt/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("menus/")
    fun getMenus(): Flow<List<Menu>>

    @GET("orders/bartender/")
    suspend fun getOrders(@Query("status") status: Int): Response<OrderResponse>

    @FormUrlEncoded
    @PUT("orders/bartender/")
    suspend fun updateOrder(
        @Query("id") orderId: Int,
        @Field("status") status: Int
    ): Response<Any>

//    @POST("/api/orders/create/")
//    suspend fun createOrder(@Body createOrder: CreateOrder): Order

    @GET("products/")
    suspend fun getProducts(@Query("search") search: String): Response<ProductResponse>

    @GET("products/{id}/")
    fun getProductById(@Path("id") id: Int): Response<Product>

    @POST("products/add/")
    suspend fun addProduct( @Body product: AddProductRequest):Response<AddProductResponse>

   @PUT("products/{id}/")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product):Response<Product>

    @PATCH("products/{id}/")
    suspend fun patchProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @DELETE("products/{id}/")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @GET("products/recepie/{productId}")
    suspend fun getRecipe(@Path("productId") productId: Int): Response<GetRecipeResponse>
    @POST("products/recepie/add/")
    suspend fun addRecipe(@Body addRecipeRequest: AddRecipeRequest): Response<AddRecipeResponse>
    @DELETE("products/recepie/detail/{productId}")
    suspend fun deleteRecipe(@Path("productId") productId: Int): Response<Unit>
    @PUT("products/recepie/detail/{productId}")
    suspend fun updateRecipe(
        @Path("productId") productId: Int, @Body addRecipeRequest: AddRecipeRequest
    ): Response<AddRecipeResponse>

    @Multipart
    @POST("products/img-add/")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUploadResponse>

}
