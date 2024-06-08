package com.aso.asomenuadmin.ui.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.model.OrderResponse
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.LoginResponse
import com.aso.asomenuadmin.network.token.TokenManager
import com.aso.asomenuadmin.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: Repository, private val tokenManager: TokenManager
) : ViewModel() {

    private val _ordersState = MutableStateFlow<ApiState<OrderResponse>>(ApiState.Idle)
    val ordersState: StateFlow<ApiState<OrderResponse>> = _ordersState

    private val _loginState = MutableStateFlow<ApiState<LoginResponse>>(ApiState.Idle)
    val loginState: StateFlow<ApiState<LoginResponse>> = _loginState

    private var loginResponse: LoginResponse? = null

    init {
        login("norouzi8446@gmail.com", "1")
        // get Orders Every 10 seconds
        viewModelScope.launch {
            while (true) {
                getOrders()
                delay(30000)
            }
        }
    }

    private fun getOrders() {
        _ordersState.value = ApiState.Loading
        viewModelScope.launch {
            repository.getOrders(orderStatus = 4).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> {
                        _ordersState.value = apiState
                        Timber.d("Orders successful: ${apiState.data}")
                    }

                    is ApiState.Failure -> {
                        _ordersState.value = apiState
                        Timber.d("Orders failed: ${apiState.errorMessage}+${apiState.errorCode}")
                    }

                    ApiState.Idle -> Timber.d("Orders idle")
                    ApiState.Loading -> Timber.d("Orders loading")
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        _loginState.value = ApiState.Loading
        viewModelScope.launch {
            repository.login(email, password).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> {
                        _loginState.value = apiState
                        loginResponse = apiState.data
                        Timber.d("Login successful: $loginResponse")
                        storeToken(loginResponse)
                    }

                    is ApiState.Failure -> {
                        _loginState.value = apiState
                        Timber.e("Login failed: ${apiState.errorMessage}")
                    }

                    else -> {
                        // Handle other cases if needed
                    }
                }
            }
        }
    }

    private suspend fun storeToken(loginResponse: LoginResponse?) {
        // Perform operations with the loginResponse data within the ViewModel
        // For example, store tokens, update user information, etc.
        if (loginResponse != null) {
            tokenManager.saveToken(loginResponse.result.access)
            tokenManager.saveRefreshToken(loginResponse.result.refresh)
            Timber.d("Access token: ${loginResponse.result.access}")
            Timber.d("Refresh token: ${loginResponse.result.refresh}")
            // Additional operations with loginResponse data
        }
    }
}


