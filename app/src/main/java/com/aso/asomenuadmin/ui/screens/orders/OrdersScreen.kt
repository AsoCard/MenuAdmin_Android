package com.aso.asomenuadmin.ui.screens.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aso.asomenuadmin.model.Order
import com.aso.asomenuadmin.model.OrderResponse
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.ui.component.OrderCard
import com.aso.asomenuadmin.ui.component.TopAppBar
import com.aso.asomenuadmin.ui.theme.LightBeige
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(
    drawerState: DrawerState,
    viewModel: OrdersViewModel = hiltViewModel(),
    onNavigateWithParam: (String, Long) -> Unit,
    orderStatus : Int = 1
) {
    val coroutineScope = rememberCoroutineScope()
    val ordersState = viewModel.ordersState.collectAsState()
    var ordersList = emptyList<Order>()

    LaunchedEffect(key1 = orderStatus) {
        viewModel.tryLoginIfTokenNotExist(orderStatus)
    }
    when (ordersState.value) {
        is ApiState.Loading -> {
            IconButton(onClick = { /*TODO*/ }) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(LightBeige.copy(alpha = 0.3f)),
                    strokeWidth = 2.dp
                )
            }
        }

        is ApiState.Success -> {
            ordersList = (ordersState.value as ApiState.Success<OrderResponse>).data.result
            IconButton(modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(LightBeige.copy(alpha = 0.3f))
                .size(32.dp),
                onClick = { viewModel.getOrders(orderStatus) }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
        is ApiState.Failure -> {
            IconButton(modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(LightBeige.copy(alpha = 0.3f))
                .size(32.dp),
                onClick = { viewModel.getOrders(orderStatus) }) {
                Icon(

                    imageVector = Icons.Default.Refresh, contentDescription = "Refresh"
                )
            }
        }
        is ApiState.Idle -> {
            IconButton(modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(LightBeige.copy(alpha = 0.3f))
                .size(32.dp),
                onClick = { viewModel.getOrders(orderStatus) }) {
                Icon(
                    imageVector = Icons.Default.Refresh, contentDescription = "Refresh"
                )
            }
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
