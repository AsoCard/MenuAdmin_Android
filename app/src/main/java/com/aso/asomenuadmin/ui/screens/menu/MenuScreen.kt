package com.aso.asomenuadmin.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.aso.asomenuadmin.model.Product
import com.aso.asomenuadmin.model.Category
import com.aso.asomenuadmin.R

@Composable
fun MenuScreen(viewModel: MenuViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF6A1B9A))) {
        Column {
            Header()
            SearchBar()
            CategorySection(state.categories)
            ProductSection(state.products)
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        state.error?.let { error ->
            Text(text = error, color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "خوش آمدید", color = Color.White, fontSize = 24.sp)
        IconButton(onClick = { /* TODO: Handle cart click */ }) {
            Icon(
                painter = painterResource(id = R.drawable.category),
                contentDescription = "Cart",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = CircleShape)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.history),
                contentDescription = "Search",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "نوشیدنی مورد علاقه‌تو پیدا کن", color = Color.Gray)
        }
    }
}

@Composable
fun CategorySection(categories: List<Category>) {
    LazyRow(modifier = Modifier.padding(8.dp)) {
        items(categories) { category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { /* TODO: Handle category click */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = category.iconUrl),
            contentDescription = category.name,
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, shape = CircleShape)
                .padding(8.dp)
        )
        Text(text = category.name, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun ProductSection(products: List<Product>) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(products) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFF8E24AA), shape = CircleShape)
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = if( product.images.isNotEmpty()) {
                product.images[0].image
            } else "http://api.420coffee.ir/media/product/images/Cappuccino_at_Sightglass_Coffee.jpg"
            ),
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
    }
}
