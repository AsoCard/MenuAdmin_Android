package com.aso.asomenuadmin.network

import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
    emit(ApiState.Loading)
    try {
        withTimeoutOrNull(2000) {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiState.Success(it))
                } ?: emit(ApiState.Failure("Response body is null", 204))
            } else {
                response.errorBody()?.let { error ->
                    val parsedError: ErrorResponse = Gson().fromJson(error.charStream(), ErrorResponse::class.java)
                    emit(ApiState.Failure(parsedError.message, parsedError.code))
                } ?: emit(ApiState.Failure("Unknown error", response.code()))
            }
        } ?: emit(ApiState.Failure("Timeout! Please try again.", 408))
    } catch (e: Exception) {
        emit(ApiState.Failure(e.message ?: "An unknown error occurred", 500))
    }
}
