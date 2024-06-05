package com.ezcall.data.dataSource.remote.entities

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("email")
    val email: String,

    @SerializedName("token")
    val token: Token,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) {
    data class Token(
        @SerializedName("refresh")
        val refresh: String,

        @SerializedName("access")
        val access: String
    )
}