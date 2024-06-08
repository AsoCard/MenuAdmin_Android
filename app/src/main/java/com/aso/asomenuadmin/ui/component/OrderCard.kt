package com.aso.asomenuadmin.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aso.asomenuadmin.model.Order
import com.aso.asomenuadmin.model.Product

@Composable
fun OrderCard(order: Order, modifier: Modifier = Modifier
) {
    val tableNumber = order.address
    val orderTime = order.createdAt
    val productItems = order.products
    if (productItems.isNullOrEmpty()) {
        // Handle the case where productItems is null or empty
        Text("No product items found.")
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        text = orderTime, style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Table $tableNumber", style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                productItems.forEach { productItem ->
                    ProductItemRow(productItem = productItem)
//                Divider(
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
                }
                Button(onClick = { /*Update Order Status to 4 */ }) {
                    Text(text = "آماده شد!")
                }
            }
        }
    }

}

@Composable
fun ProductItemRow(
    productItem: Product,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductItem(
            productItem,
            onRecipeClick = { }
        )

    }
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


