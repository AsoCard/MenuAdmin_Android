package com.aso.asomenuadmin.network

import com.aso.asomenuadmin.model.MenuItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("endpoint") // Replace with your endpoint
    suspend fun getData(): Response<List<MenuItem>>
}
