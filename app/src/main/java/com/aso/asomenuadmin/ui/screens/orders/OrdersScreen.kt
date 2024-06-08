package com.aso.asomenuadmin.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aso.asomenuadmin.model.Order
import com.aso.asomenuadmin.model.OrderResponse
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.ui.component.OrderCard
import com.aso.asomenuadmin.ui.component.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(
    drawerState: DrawerState,
    viewModel: OrdersViewModel = hiltViewModel(),
    onNavigateWithParam: (String, Long) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val ordersState = viewModel.ordersState.collectAsState()
    var ordersList = emptyList<Order>()

    when (ordersState.value) {
        is ApiState.Loading -> {
            Text("Loading")
        }

        is ApiState.Success -> {
            ordersList = (ordersState.value as ApiState.Success<OrderResponse>).data.result
        }
        is ApiState.Failure -> {
            Text("Error")
        }
        is ApiState.Idle -> {
            Text("Idle")
        }

    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = "لیست سفارشات",
            onMenuClick = { coroutineScope.launch { drawerState.open() } }
        )
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(ordersList) { orderData ->
                OrderCard(orderData,onNavigateWithParam = onNavigateWithParam)
            }
        }
    }
}
