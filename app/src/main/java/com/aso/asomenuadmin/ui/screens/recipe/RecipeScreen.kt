package com.aso.asomenuadmin.ui.screens.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter

@Composable
fun RecipeScreen(productId: Int, viewModel: RecipeViewModel = hiltViewModel()) {
    LaunchedEffect(productId) {
        viewModel.handleIntent(RecipeIntent.LoadRecipe(productId))
    }

    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = state.title, style = MaterialTheme.typography.bodySmall)

        Image(
            painter = rememberImagePainter(state.imageUrl),
            contentDescription = null,
            modifier = Modifier.height(200.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))
        )

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
