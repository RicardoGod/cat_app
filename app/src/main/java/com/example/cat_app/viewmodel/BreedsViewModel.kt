package com.example.cat_app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.viewmodel.state.BreedsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BreedsViewModel(private val breedsService: IBreedsService) : ViewModel() {
    val state = MutableStateFlow(BreedsUiState())

    fun fetchBreeds(context: Context, loadMore: Boolean = false) {
        viewModelScope.launch {
            val result = breedsService.getBreedsList(
                limit = state.value.pageSize,
                page = state.value.currentPage
            )
            state.value = when {
                result.isSuccess -> BreedsUiState(data = result.getOrThrow())
                else -> BreedsUiState(error = "Failed to load breeds")
            }
        }
    }

    fun searchBreeds(context: Context, query: String) {
        viewModelScope.launch {
            val result = breedsService.searchBreeds(query)
            state.value = when {
                result.isSuccess -> BreedsUiState(data = result.getOrThrow())
                else -> BreedsUiState(error = "Failed to load breeds")
            }
        }
    }

    fun getBreedImageUrl(breed: BreedsModel): String {
        return breed.image?.url
            ?: breed.referenceImageId?.let { "https://cdn2.thecatapi.com/images/$it.jpg" }
            ?: "https://cdn2.thecatapi.com/images/default.jpg"
    }

}