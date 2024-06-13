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
    private val repository: Repository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _ordersState = MutableStateFlow<ApiState<OrderResponse>>(ApiState.Idle)
    val ordersState: StateFlow<ApiState<OrderResponse>> = _ordersState

    private val _loginState = MutableStateFlow<ApiState<LoginResponse>>(ApiState.Idle)
    val loginState: StateFlow<ApiState<LoginResponse>> = _loginState

    private var loginResponse: LoginResponse? = null

//    init {
//        tryLoginIfTokenNotExist()
//    }

     fun tryLoginIfTokenNotExist(orderStatus: Int) {
        viewModelScope.launch {
            while (true) {
                val tokenExists = tokenManager.tokenExists()
                if (!tokenExists || _loginState.value !is ApiState.Success) {
                    login("norouzi8446@gmail.com", "1",orderStatus)
                }
                delay(10000)
            }
        }
    }

    fun updateOrderStatus(orderId: Int, orderStatus: Int) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, orderStatus).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> {
                        Timber.d("Order status updated successfully")
                        getOrders(1)
                    }

                    is ApiState.Failure -> {
                        Timber.d("Order status update failed: ${apiState.message}")
                    }

                    ApiState.Idle -> Timber.d("Order status update Idle")
                    ApiState.Loading -> Timber.d("Order status update Loading")
                }
            }
        }
    }

    fun getOrders(orderStatus: Int) {
        viewModelScope.launch {
            repository.getOrders(orderStatus = orderStatus).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> {
                        val newData = apiState.data
                        val previousData = (_ordersState.value as? ApiState.Success)?.data
                        if (newData != previousData) {
                            _ordersState.value = apiState
                            Timber.d("Orders successful: $newData")
                        } else {
                            Timber.d("Orders data unchanged")
                        }
                    }

                    is ApiState.Failure -> {
                        _ordersState.value = apiState
                        Timber.d("Orders failed: ${apiState.message}+${apiState.errorCode}")
                    }

                    ApiState.Idle -> {
                        Timber.d("Orders idle")
                    }

                    ApiState.Loading -> {
                        Timber.d("Orders loading")
                    }
                }
            }
        }
    }


    private fun login(email: String, password: String, orderStatus: Int) {
        _loginState.value = ApiState.Loading
        viewModelScope.launch {
            repository.login(email, password).collect { apiState ->
                when (apiState) {
                    is ApiState.Success -> {
                        _loginState.value = apiState
                        loginResponse = apiState.data
                        Timber.d("Login successful: $loginResponse")
                        storeToken(loginResponse)
                        // After successful login, start fetching orders
                        fetchOrdersPeriodically(orderStatus)
                    }

                    is ApiState.Failure -> {
                        _loginState.value = apiState
                        Timber.e("Login failed: ${apiState.message}")
                    }

                    else -> {
                        // Handle other cases if needed
                    }
                }
            }
        }
    }

    private suspend fun storeToken(loginResponse: LoginResponse?) {
        loginResponse?.let {
            tokenManager.saveToken(it.result.access)
            tokenManager.saveRefreshToken(it.result.refresh)
            Timber.d("Access token: ${it.result.access}")
            Timber.d("Refresh token: ${it.result.refresh}")
        }
    }

    private fun fetchOrdersPeriodically(orderStatus: Int) {
        viewModelScope.launch {
            while (true) {
                getOrders(orderStatus)
                delay(2000) // Fetch orders every 10 seconds
            }
        }
    }
}
