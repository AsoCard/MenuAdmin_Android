package com.aso.asomenuadmin.network.entities

import com.google.gson.annotations.SerializedName

data class LoginRequest(@SerializedName("email") val userName: String, val password: String)
