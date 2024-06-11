package com.aso.asomenuadmin.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aso.asomenuadmin.model.Order
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.ui.navigation.MainDestinations
import com.aso.asomenuadmin.ui.screens.orders.OrdersViewModel
import com.aso.asomenuadmin.ui.theme.Brown
import com.aso.asomenuadmin.ui.theme.LightBeige
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrderCard(
    order: Order,
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = hiltViewModel(),
    onNavigateWithParam: (String, Long) -> Unit
) {
    val tableNumber = order.address
    val orderTime = order.createdAt
    val productItems = order.products
    if (productItems.isNullOrEmpty()) {
        // Handle the case where productItems is null or empty
        Text("No product items found.")
    } else {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "میز شماره  $tableNumber",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "ساعت ثبت سفارش ${extractHourMinute(orderTime)}",
                         style = MaterialTheme.typography.bodySmall
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                productItems.forEach { productItem ->
                    ProductItemRow(productItem = productItem,onNavigateWithParam = onNavigateWithParam)

                }
                Button(
                    colors = ButtonDefaults.buttonColors().copy(containerColor = LightBeige),
                    modifier = Modifier.align(Alignment.End).width(150.dp).height(33.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                    viewModel.updateOrderStatus(order.id, 4)

                }) {
                    Text(color = Brown,text = "آماده تحویل")
                }
            }
        }
    }

}

@Composable
fun ProductItemRow(
    productItem: Product,
    modifier: Modifier = Modifier,
    onNavigateWithParam: (String, Long) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductItem(
            productItem, onRecipeClick = {
                onNavigateWithParam(MainDestinations.RECIPE_ROUTE, productItem.id.toLong(),)
            }
        )

    }
}
fun extractHourMinute(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    val offsetDateTime = OffsetDateTime.parse(dateTimeString, formatter)
    val hourMinute = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    return hourMinute
}
//
//@Preview(showBackground = true)
//@Composable
//fun OrderCardPreview() {
//    MenuAdminTheme {
//        OrderCard(
//            tableNumber = "12",
//            orderTime = "12:30 PM",
//            productItems = listOf(
//                Product(1, "10.99", listOf(Image),"10.99"),
//                Product(2, "3.99","3.99","3.99"),
//                Product(3, "2.50","2.50","2.50")
//            )
//        )
//    }
//}


