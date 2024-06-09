package com.aso.asomenuadmin.network

import com.aso.asomenuadmin.network.entities.LoginRequest
import com.ezcall.data.dataSource.remote.entities.SignUpRequest
import com.aso.asomenuadmin.network.entities.LoginResponse
import com.ezcall.data.dataSource.remote.entities.SignUpResponse
import com.ezcall.data.dataSource.remote.entities.TokenVerify
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/jwt/login/")
    suspend fun loginUser(@Body authLogin: LoginRequest): Response<LoginResponse>

    @POST("api/users/register/")
    suspend fun signUpUser(@Body authSignUp: SignUpRequest): Response<SignUpResponse>

    @POST("api/auth/verify/")
    suspend fun verifyToken(@Body token: TokenVerify): Response<LoginResponse>

    @POST("api/auth/jwt/refresh/")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<LoginResponse>

}