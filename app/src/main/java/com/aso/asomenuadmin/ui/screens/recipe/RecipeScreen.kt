package com.aso.asomenuadmin.ui.screens.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.aso.asomenuadmin.R
import com.aso.asomenuadmin.ui.component.TopAppBar
import com.aso.asomenuadmin.ui.theme.LightBeige
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(
    productId: Long, viewModel: RecipeViewModel = hiltViewModel(), upPress: () -> Unit
) {

    val state by viewModel.state.collectAsState()
    val imageUrl = state.imageUrl.firstOrNull()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .placeholder(R.drawable.orders)
            .error(R.drawable.category)
            .fallback(R.drawable.history)
            .build())

    LaunchedEffect(productId) {
        viewModel.handleIntent(RecipeIntent.LoadRecipe(productId.toInt()))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.recipe),
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

    }

    IconButton(modifier = Modifier
        .padding(16.dp)
        .clip(CircleShape)
        .background(LightBeige.copy(alpha = 0.3f))
        .size(32.dp),
        onClick = { upPress.invoke() }) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Refresh"
        )
    }




    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
//        Text(text = state.title, style = MaterialTheme.typography.bodySmall)

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.padding(top = 32.dp)
                .height(250.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = state.title, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "مواد لازم:", style = MaterialTheme.typography.bodySmall)
        state.ingredients.forEach { ingredient ->
            Text(text = "• $ingredient")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "طرز تهیه:", style = MaterialTheme.typography.bodySmall)
        state.steps.forEachIndexed { index, step ->
            Text(text = "${index + 1}. $step")
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.videoUrl?.let { videoUrl ->
            VideoPlayer(videoUrl)
        }
    }
}

@Composable
fun VideoPlayer(videoUrl: String) {
    // Implement your video player here
}
