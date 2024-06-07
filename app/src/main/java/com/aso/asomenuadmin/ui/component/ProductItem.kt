package com.aso.asomenuadmin.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.aso.asomenuadmin.model.Product

@Composable
fun ProductItem(
    product: Product,
    onRecipeClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = product.images.get(0).image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = product.name)
            Text(text = product.detail)
        }

        Spacer(modifier = Modifier.width(8.dp))

        TextButton(
            onClick = { onRecipeClick(product.id.toString()) },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "Recipe")
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ProductItemPreview() {
//    val product = Product(
//        id = "1",
//        name = "Product Name",
//        subtitle = "Product Subtitle",
//        imageUrl = "https://example.com/product-image.jpg"
//    )
//
//    ProductItem(
//        product = product,
//        onRecipeClick = { /* Handle recipe click */ }
//    )
//}
