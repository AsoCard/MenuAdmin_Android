package com.aso.asomenuadmin.intent

sealed class AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent()
}