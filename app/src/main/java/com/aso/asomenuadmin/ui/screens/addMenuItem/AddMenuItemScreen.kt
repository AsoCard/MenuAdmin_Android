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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.ImageUploadResponse
import com.aso.asomenuadmin.ui.theme.BlackBrown
import com.aso.asomenuadmin.ui.theme.LightBeige
import kotlinx.coroutines.launch

@Composable
fun AddMenuItemScreen(
    viewModel: AddMenuItemViewModel = hiltViewModel(),
    onUpPress: () -> Unit,
    productId: Long
) {
    val state by viewModel.state.collectAsState()
    val focusRequesters = remember { List(7) { FocusRequester() } }

    LaunchedEffect(productId) {
        viewModel.initialize(productId)
    }
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
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.TitleChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[0]),
            onNext = { focusRequesters[1].requestFocus() }
        )
        AddMenuItemInputField(
            label = "عنوان فرعی",
            value = state.subtitle,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.SubtitleChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[1]),
            onNext = { focusRequesters[2].requestFocus() }
        )
        AddMenuItemInputField(
            label = "توضیحات",
            value = state.description,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.DescriptionChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[2]),
            onNext = { focusRequesters[3].requestFocus() }
        )
        AddMenuItemInputField(
            label = "مواد لازم",
            value = state.ingredients,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.IngredientsChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[3]),
            onNext = { focusRequesters[4].requestFocus() }
        )
        AddMenuItemInputField(
            label = "طرز تهیه",
            value = state.preparationMethod,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.PreparationMethodChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[4]),
            onNext = { focusRequesters[5].requestFocus() }
        )
        AddMenuItemInputField(
            label = "نحوه سرو کردن",
            value = state.servingMethod,
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.ServingMethodChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[5]),
            onNext = { focusRequesters[6].requestFocus() }
        )
        AddMenuItemInputField(
            label = "قیمت",
            value = state.price.toString(),
            onValueChange = { viewModel.handleEvent(AddMenuItemEvent.PriceChanged(it)) },
            modifier = Modifier.focusRequester(focusRequesters[6]),
            onDone = {
                viewModel.handleEvent(AddMenuItemEvent.Submit)
                onUpPress.invoke()
            },
            isPriceField = true
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
                onUpPress.invoke()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "ثبت آیتم منو")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuItemInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onDone: () -> Unit = {},
    isPriceField: Boolean = false
) {
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isPriceField) {
                try {
                    it.toInt()
                    errorMessage = ""
                    onValueChange(it)
                } catch (e: NumberFormatException) {
                    errorMessage = "قیمت باید عدد باشد"
                }
            } else {
                errorMessage = ""
                onValueChange(it)
            }
        },
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
        singleLine = true,
        isError = errorMessage.isNotEmpty(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                scope.launch { onNext() }
            },
            onDone = {
                scope.launch { onDone() }
            }
        ),
        keyboardOptions = KeyboardOptions().copy(
            imeAction = if (onNext != {}) ImeAction.Next else ImeAction.Done
        )
    )
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun AddCategoryDropdown(selectedCategory: Int, onCategorySelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("نوشیدنی گرم", "نوشیدنی سرد", "کیک", "بیرون بر")

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
                    text = if (selectedCategory == -1) { "دسته بندی" } else categories[selectedCategory],
                    color = if (selectedCategory== -1) Color.Gray else MaterialTheme.colorScheme.onSurface,
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
            categories.forEachIndexed { index,category ->
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        // ADDED BY 3 TO MATCH SERVER IDS OF CATEGORY
                        onCategorySelected(index)
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
                .clip(RoundedCornerShape(16.dp))
                .background(LightBeige)
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
                Text(text = "برای افزودن عکس لمس کنید", color = BlackBrown)
            }
        }
        when (uploadState) {
            is ApiState.Loading -> CircularProgressIndicator()
            is ApiState.Failure -> Text("اپلود ناموفق", color = Color.Red)
            is ApiState.Success -> Text("آپلود موفق", color = Color.Green)
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
