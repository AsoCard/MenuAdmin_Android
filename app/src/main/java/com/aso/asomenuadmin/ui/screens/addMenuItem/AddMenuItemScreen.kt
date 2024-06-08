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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import coil.compose.rememberAsyncImagePainter

@Composable
fun AddMenuItemScreen(viewModel: AddMenuItemViewModel = hiltViewModel()) {
    val title by viewModel.title.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()
    val description by viewModel.description.collectAsState()
    val ingredients by viewModel.ingredients.collectAsState()
    val preparationMethod by viewModel.preparationMethod.collectAsState()
    val servingMethod by viewModel.servingMethod.collectAsState()
    val price by viewModel.price.collectAsState()
    val category by viewModel.category.collectAsState()
    val mainImage by viewModel.mainImage.collectAsState()
    val ideaImage by viewModel.ideaImage.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()

    val galleryLauncherMain =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setMainImage(it)
            }
        }

    val galleryLauncherIdea =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setIdeaImage(it)
            }
        }

    val galleryLauncherVideo =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setVideoUri(it)
            }
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
            label = "عنوان اصلی", value = title, onValueChange = viewModel::onTitleChange
        )
        AddMenuItemInputField(
            label = "عنوان فرعی", value = subtitle, onValueChange = viewModel::onSubtitleChange
        )
        AddMenuItemInputField(
            label = "توضیحات", value = description, onValueChange = viewModel::onDescriptionChange
        )
        AddMenuItemInputField(
            label = "مواد لازم", value = ingredients, onValueChange = viewModel::onIngredientsChange
        )
        AddMenuItemInputField(
            label = "طرز تهیه",
            value = preparationMethod,
            onValueChange = viewModel::onPreparationMethodChange
        )
        AddMenuItemInputField(
            label = "نحوه سرو کردن",
            value = servingMethod,
            onValueChange = viewModel::onServingMethodChange
        )
        AddMenuItemInputField(
            label = "قیمت", value = price, onValueChange = viewModel::onPriceChange
        )

        AddCategoryDropdown(
            selectedCategory = category, onCategorySelected = viewModel::onCategoryChange
        )

        AddImageSection(label = "افزودن تصویر اصلی",
            imageBitmap = mainImage,
            onImageClick = { galleryLauncherMain.launch("image/*") })

        AddImageSection(label = "افزودن تصویر برای ایده",
            imageBitmap = ideaImage,
            onImageClick = { galleryLauncherIdea.launch("image/*") })

        AddVideoSection(label = "افزودن ویدئو",
            videoUri = videoUri,
            onVideoClick = { galleryLauncherVideo.launch("video/*") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle submit */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "تایید")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* Handle cancel */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "بازگشت")
        }
    }
}

@Composable
fun AddMenuItemInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFD1D1D6))
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C2C2E))
                .padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDropdown(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Cold Drink", "Hot Drink", "Cake", "Take Away")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedCategory) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("دسته‌بندی", color = Color(0xFFD1D1D6)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF2C2C2E),
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                unfocusedLabelColor = Color(0xFFD1D1D6)
            )
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = category
                        expanded = false
                        onCategorySelected(category)
                    },
                    text = { Text(text = category) }
                )
            }
        }
    }
}

@Composable
fun AddImageSection(label: String, imageBitmap: Bitmap?, onImageClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFD1D1D6))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF2C2C2E))
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Text(text = "تصویر را به صفحه بکشید یا آپلود تصویر", color = Color(0xFFD1D1D6))
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFD1D1D6))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF2C2C2E))
                .clickable { onVideoClick() },
            contentAlignment = Alignment.Center
        ) {
            videoUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Text(text = "تصویر را به صفحه بکشید یا آپلود ویدئو", color = Color(0xFFD1D1D6))
        }
    }
}