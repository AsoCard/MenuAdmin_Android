package com.ezcall.data.dataSource.remote.entities

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access")
    val accessToken: String,
    @SerializedName("refresh")
    val refreshToken: String,
)
