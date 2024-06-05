package com.ezcall.data.dataSource.remote.entities

import com.google.gson.annotations.SerializedName

data class AuthSignUp(
    val email: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String,
)
