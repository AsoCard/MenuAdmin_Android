package com.aso.asomenuadmin.network.entities

import com.google.gson.annotations.SerializedName

data class LoginRequest(@SerializedName("phone_number") val phoneNumber: String, val password: String)
