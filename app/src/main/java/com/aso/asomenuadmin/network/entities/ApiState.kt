package com.aso.asomenuadmin.network.entities

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    object Idle : ApiState<Nothing>()

    data class Success<out T>(val data: T) : ApiState<T>()

    data class Failure(val errorMessage: String, val errorCode: Int) : ApiState<Nothing>()
}