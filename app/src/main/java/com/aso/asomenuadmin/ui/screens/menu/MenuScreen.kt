package com.aso.asomenuadmin.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.ui.theme.BlackBrown
import com.aso.asomenuadmin.ui.theme.Brown
import com.aso.asomenuadmin.ui.theme.LightBeige

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (Product) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "لیست محصولات") },
                actions = {
                    IconButton(onClick = { viewModel.sendIntent(MenuIntent.LoadProducts) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddProduct, containerColor = LightBeige) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(BlackBrown)
            .padding(innerPadding)) {

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ProductSection(
                    products = state.products,
                    onEditProduct = { product ->
                        onNavigateToEditProduct(product)
                    },
                    onDeleteProduct = { productId ->
                        viewModel.sendIntent(MenuIntent.DeleteProduct(productId))
                    }
                )

                state.error?.let { error ->
                    Text(text = error, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun ProductSection(
    products: List<Product>,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(products) { product ->
            ProductItem(
                product = product,
                onEditProduct = onEditProduct,
                onDeleteProduct = onDeleteProduct
            )
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Brown, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = if (product.images.isNotEmpty()) {
                product.images[0].image
            } else {
                "http://api.420coffee.ir/media/product/images/Cappuccino_at_Sightglass_Coffee.jpg"
            }),
            contentDescription = product.name,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.name, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = product.price.toString(), color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = product.detail, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onEditProduct(product) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
            IconButton(onClick = { onDeleteProduct(product.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }
    }
}
