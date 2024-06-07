package com.aso.asomenuadmin.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aso.asomenuadmin.model.Image
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.ui.component.OrderCard
import com.aso.asomenuadmin.ui.component.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    val ordersList = listOf(
        OrderData(
            tableNumber = "13",
            orderTime = "17:59",
            productItems = listOf(
                Product(1, "کاپوچینو", listOf(Image(1, "", "", "")), "2", "", "", "", 10, ""),
                Product(1, "کاپوچینو", listOf(Image(1, "", "", "")), "2", "", "", "", 10, ""),
            )
        ),
        OrderData(
            tableNumber = "13",
            orderTime = "17:59",
            productItems = listOf(
                Product(1, "کاپوچینو", listOf(Image(1, "", "", "")), "2", "", "", "", 10, ""),
                Product(1, "کاپوچینو", listOf(Image(1, "", "", "")), "2", "", "", "", 10, ""),
            )
        )
    )

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = "لیست سفارشات",
            onMenuClick = { coroutineScope.launch { drawerState.open() } }
        )
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(ordersList) { orderData ->
                OrderCard(
                    tableNumber = orderData.tableNumber,
                    orderTime = orderData.orderTime,
                    productItems = orderData.productItems
                )
            }
        }
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { }
        ) {
            Text("ادامه فرایند")
        }
    }
}

data class OrderData(
    val tableNumber: String,
    val orderTime: String,
    val productItems: List<Product>
)