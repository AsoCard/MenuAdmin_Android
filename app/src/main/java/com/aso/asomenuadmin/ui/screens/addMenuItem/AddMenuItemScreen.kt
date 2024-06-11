package com.aso.asomenuadmin.ui.screens.addMenuItem

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.ImageUploadResponse

@Composable
fun AddMenuItemScreen(viewModel: AddMenuItemViewModel = hiltViewModel(), onUpPress: () -> Unit) {
    val state by viewModel.state.collectAsState()

    val galleryLauncherMain =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.handleEvent(AddMenuItemEvent.MainImageChanged(it)) }
        }

    val galleryLauncherIdea =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.handleEvent(AddMenuItemEvent.IdeaImageChanged(it)) }
        }

    val galleryLauncherVideo =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.handleEvent(AddMenuItemEvent.VideoUriChanged(it)) }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "افزودن آیتم منو",
            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AddMenuItemInputField(
            label = "عنوان اصلی",
            value = state.title,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.TitleChanged(it)) }
        )
        AddMenuItemInputField(
            label = "عنوان فرعی",
            value = state.subtitle,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.SubtitleChanged(it)) }
        )
        AddMenuItemInputField(
            label = "توضیحات",
            value = state.description,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.DescriptionChanged(it)) }
        )
        AddMenuItemInputField(
            label = "مواد لازم",
            value = state.ingredients,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.IngredientsChanged(it)) }
        )
        AddMenuItemInputField(
            label = "طرز تهیه",
            value = state.preparationMethod,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.PreparationMethodChanged(it)) }
        )
        AddMenuItemInputField(
            label = "نحوه سرو کردن",
            value = state.servingMethod,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.ServingMethodChanged(it)) }
        )
        AddMenuItemInputField(
            label = "قیمت",
            value = state.price,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.PriceChanged(it)) }
        )

        AddCategoryDropdown(
            selectedCategory = state.category,
            onCategorySelected = { viewModel.handleEvent(AddMenuItemEvent.CategoryChanged(it)) }
        )

        AddImageSection(label = "افزودن تصویر اصلی",
            imageBitmap = state.mainImage,
            onImageClick = { galleryLauncherMain.launch("image/*") },
            uploadState = state.mainImageUploadState
        )

        AddImageSection(label = "افزودن تصویر برای ایده",
            imageBitmap = state.ideaImage,
            onImageClick = { galleryLauncherIdea.launch("image/*") },
            uploadState = state.ideaImageUploadState
        )

//        AddVideoSection(
//            label = "افزودن ویدئو", videoUri = state.videoUri,
//            onVideoClick = { galleryLauncherVideo.launch("video/*") })

        Button(
            onClick = {
                viewModel.handleEvent(AddMenuItemEvent.Submit)
                viewModel.handleEvent(AddMenuItemEvent.AddRecipeClicked)
                onUpPress.invoke()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "ثبت آیتم منو")
        }

//        Button(
//            onClick = { viewModel.handleEvent(AddMenuItemEvent.AddRecipeClicked) },
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        ) {
//            Text(text = "ثبت دستور غذا")
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuItemInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray
        )
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDropdown(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Category 1", "Category 2", "Category 3")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            onClick = { expanded = !expanded },
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedCategory.ifEmpty { "دسته بندی" },
                    color = if (selectedCategory.isEmpty()) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}



@Composable
fun AddImageSection(
    label: String,
    imageBitmap: Bitmap?,
    onImageClick: () -> Unit,
    uploadState: ApiState<ImageUploadResponse>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray)
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "Tap to add image", color = Color.White)
            }
        }
        when (uploadState) {
            is ApiState.Loading -> CircularProgressIndicator()
            is ApiState.Failure -> Text("Upload Failed", color = Color.Red)
            is ApiState.Success -> Text("Upload Success", color = Color.Green)
            else -> {}
        }
    }
}

@Composable
fun AddVideoSection(label: String, videoUri: Uri?, onVideoClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray)
                .clickable { onVideoClick() },
            contentAlignment = Alignment.Center
        ) {
            if (videoUri != null) {
                Text(text = "Video selected", color = Color.White)
            } else {
                Text(text = "Tap to add video", color = Color.White)
            }
        }
    }
}
