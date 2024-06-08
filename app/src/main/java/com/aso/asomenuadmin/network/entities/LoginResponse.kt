package com.aso.asomenuadmin.network.entities

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("result")
    val result: TokenResponse,
    @SerializedName("message")
    val message: String
) {
    data class TokenResponse(
        @SerializedName("refresh")
        val refresh: String,
        @SerializedName("access")
        val access: String
    )
}
