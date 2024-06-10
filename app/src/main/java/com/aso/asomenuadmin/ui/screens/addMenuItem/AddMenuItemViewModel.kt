package com.aso.asomenuadmin.ui.screens.addMenuItem

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.asomenuadmin.network.entities.ApiState
import com.aso.asomenuadmin.network.entities.ImageUploadResponse
import com.aso.asomenuadmin.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMenuItemViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> get() = _title

    private val _subtitle = MutableStateFlow("")
    val subtitle: StateFlow<String> get() = _subtitle

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description

    private val _ingredients = MutableStateFlow("")
    val ingredients: StateFlow<String> get() = _ingredients

    private val _preparationMethod = MutableStateFlow("")
    val preparationMethod: StateFlow<String> get() = _preparationMethod

    private val _servingMethod = MutableStateFlow("")
    val servingMethod: StateFlow<String> get() = _servingMethod

    private val _mainImage = MutableStateFlow<Bitmap?>(null)
    val mainImage: StateFlow<Bitmap?> get() = _mainImage

    private val _ideaImage = MutableStateFlow<Bitmap?>(null)
    val ideaImage: StateFlow<Bitmap?> get() = _ideaImage

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> get() = _videoUri

    private val _price = MutableStateFlow("")
    val price: StateFlow<String> get() = _price

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> get() = _category



    private val _uploadState = MutableStateFlow<ApiState<ImageUploadResponse>>(ApiState.Idle)
    val uploadState: StateFlow<ApiState<ImageUploadResponse>> get() = _uploadState


    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onSubtitleChange(newSubtitle: String) {
        _subtitle.value = newSubtitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onIngredientsChange(newIngredients: String) {
        _ingredients.value = newIngredients
    }

    fun onPreparationMethodChange(newPreparationMethod: String) {
        _preparationMethod.value = newPreparationMethod
    }

    fun onServingMethodChange(newServingMethod: String) {
        _servingMethod.value = newServingMethod
    }

    fun onPriceChange(newPrice: String) {
        _price.value = newPrice
    }

    fun onCategoryChange(newCategory: String) {
        _category.value = newCategory
    }

    fun setMainImage(uri: Uri) {
        viewModelScope.launch {
            _mainImage.value = repository.compressImage(uri)
        }
    }

    fun setIdeaImage(uri: Uri) {
        viewModelScope.launch {
            _ideaImage.value = repository.compressImage(uri)
        }
    }

    fun setVideoUri(uri: Uri) {
        _videoUri.value = uri
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            repository.uploadImage(uri).collect { state ->
                _uploadState.value = state
            }
        }
    }
}
