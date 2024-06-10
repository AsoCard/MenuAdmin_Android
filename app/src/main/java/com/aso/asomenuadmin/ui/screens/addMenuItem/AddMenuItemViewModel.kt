package com.aso.asomenuadmin.ui.screens.addMenuItem

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.network.AddRecipeRequest
import com.aso.asomenuadmin.network.entities.AddProductRequest
import com.aso.asomenuadmin.network.entities.AddProductResponse
import com.aso.asomenuadmin.network.entities.AddRecipeResponse
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.ImageUploadResponse
import com.aso.asomenuadmin.repository.ImageRepository
import com.aso.asomenuadmin.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class AddMenuItemState(
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val ingredients: String = "",
    val preparationMethod: String = "",
    val servingMethod: String = "",
    val price: String = "",
    val category: String = "",
    val mainImage: Bitmap? = null,
    val ideaImage: Bitmap? = null,
    val videoUri: Uri? = null,
    val mainImageUploadState: ApiState<ImageUploadResponse> = ApiState.Idle,
    val ideaImageUploadState: ApiState<ImageUploadResponse> = ApiState.Idle,
    val addProductState: ApiState<AddProductResponse> = ApiState.Idle,
    val addRecipeState: ApiState<AddRecipeResponse> = ApiState.Idle,
    val imageIdsToUpload: MutableList<Int> = mutableListOf()
)

sealed class AddMenuItemEvent {
    data class TitleChanged(val title: String) : AddMenuItemEvent()
    data class SubtitleChanged(val subtitle: String) : AddMenuItemEvent()
    data class DescriptionChanged(val description: String) : AddMenuItemEvent()
    data class IngredientsChanged(val ingredients: String) : AddMenuItemEvent()
    data class PreparationMethodChanged(val preparationMethod: String) : AddMenuItemEvent()
    data class ServingMethodChanged(val servingMethod: String) : AddMenuItemEvent()
    data class PriceChanged(val price: String) : AddMenuItemEvent()
    data class CategoryChanged(val category: String) : AddMenuItemEvent()
    data class MainImageChanged(val uri: Uri) : AddMenuItemEvent()
    data class IdeaImageChanged(val uri: Uri) : AddMenuItemEvent()
    data class VideoUriChanged(val uri: Uri) : AddMenuItemEvent()
    object Submit : AddMenuItemEvent()
    object AddRecipeClicked : AddMenuItemEvent()
}

@HiltViewModel
class AddMenuItemViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(AddMenuItemState())
    val state: StateFlow<AddMenuItemState> get() = _state

    fun handleEvent(event: AddMenuItemEvent) {
        when (event) {
            is AddMenuItemEvent.TitleChanged -> _state.value =
                _state.value.copy(title = event.title)

            is AddMenuItemEvent.SubtitleChanged -> _state.value =
                _state.value.copy(subtitle = event.subtitle)

            is AddMenuItemEvent.DescriptionChanged -> _state.value =
                _state.value.copy(description = event.description)

            is AddMenuItemEvent.IngredientsChanged -> _state.value =
                _state.value.copy(ingredients = event.ingredients)

            is AddMenuItemEvent.PreparationMethodChanged -> _state.value =
                _state.value.copy(preparationMethod = event.preparationMethod)

            is AddMenuItemEvent.ServingMethodChanged -> _state.value =
                _state.value.copy(servingMethod = event.servingMethod)

            is AddMenuItemEvent.PriceChanged -> _state.value =
                _state.value.copy(price = event.price)

            is AddMenuItemEvent.CategoryChanged -> _state.value =
                _state.value.copy(category = event.category)

            is AddMenuItemEvent.MainImageChanged -> uploadMainImage(event.uri)
            is AddMenuItemEvent.IdeaImageChanged -> uploadIdeaImage(event.uri)
            is AddMenuItemEvent.VideoUriChanged -> _state.value =
                _state.value.copy(videoUri = event.uri)

            is AddMenuItemEvent.Submit -> onSubmit()
            is AddMenuItemEvent.AddRecipeClicked -> onAddRecipeClicked()
        }
    }

    private fun uploadMainImage(uri: Uri) {
        viewModelScope.launch {
            imageRepository.compressImage(uri)?.let {
                _state.value = _state.value.copy(mainImage = it)
                imageRepository.uploadImage(uri).collect { state ->
                    _state.value = _state.value.copy(mainImageUploadState = state)
                    when(state) {
                        is ApiState.Failure -> Timber.e("Failure: ${state.message}")
                        is ApiState.Idle -> Timber.d("Idle...")
                        is ApiState.Loading -> Timber.d("Loading...")
                        is ApiState.Success -> {
                            val imageId = state.data.result.id
                            val updatedIdsList = _state.value.imageIdsToUpload.toMutableList()
                            updatedIdsList.add(imageId)
                            _state.value = _state.value.copy(imageIdsToUpload = updatedIdsList)
                        }
                    }
                }
            }
        }
    }

    private fun uploadIdeaImage(uri: Uri) {
        viewModelScope.launch {
            imageRepository.compressImage(uri)?.let {
                _state.value = _state.value.copy(ideaImage = it)
                imageRepository.uploadImage(uri).collect { state ->
                    _state.value = _state.value.copy(ideaImageUploadState = state)

                    when(state) {
                        is ApiState.Failure -> Timber.e("Failure: ${state.message}")
                        is ApiState.Idle -> Timber.d("Idle...")
                        is ApiState.Loading -> Timber.d("Loading...")
                        is ApiState.Success -> {
                            val imageId = state.data.result.id
                            val updatedIdsList = _state.value.imageIdsToUpload.toMutableList()
                            updatedIdsList.add(imageId)
                            _state.value = _state.value.copy(imageIdsToUpload = updatedIdsList)
                        }
                    }
                }
            }
        }
    }

    private fun onSubmit() {
        onAddRecipeClicked()
        viewModelScope.launch {
            repository.addProduct(
                AddProductRequest(
                    category = 1,
                    images = state.value.imageIdsToUpload,
                    name = state.value.title,
                    detail = state.value.description,
                    price = state.value.price.toDoubleOrNull() ?: 0.0,
                    ingredients = state.value.ingredients
                )
            ).collect { apiState ->
                _state.value = _state.value.copy(addProductState = apiState)
            }
        }
    }

    private fun onAddRecipeClicked() {
        viewModelScope.launch {
            repository.addRecipe(
                AddRecipeRequest(
                    created_at = getCurrentDateTimeAsString(),
                    title = state.value.title,
                    ingredients = state.value.ingredients,
                    steps = state.value.preparationMethod,
                    img = state.value.mainImage?.toString(),
                    video = state.value.videoUri?.toString(),
                    product = 0L // Replace with actual product ID
                )
            ).collect { apiState ->
                _state.value = _state.value.copy(addRecipeState = apiState)
            }
        }
    }

    private fun getCurrentDateTimeAsString(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
