package com.example.cat_app.ui.features.favourites

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.models.FavouriteRequestModel
import com.example.cat_app.data.services.IFavouritesService
import kotlinx.coroutines.launch

class FavouritesViewModel(private val favouritesService: IFavouritesService) : ViewModel() {

    val state = mutableStateOf(FavouritesUiState())

    fun fetchFavorites(context: Context) {
        viewModelScope.launch {
            state.value = getFavouriteList()
        }
    }

    private suspend fun getFavouriteList(): FavouritesUiState {
        val result = favouritesService.getFavorites()
        return when {
            result.isSuccess -> FavouritesUiState(data = result.getOrThrow())
            else -> FavouritesUiState(error = "Failed to load favorites")
        }
    }

    //adds item to the favorites list
    fun addFavorite(context: Context, breed: BreedsModel) {
        viewModelScope.launch {
            val fav = FavouriteRequestModel(imageId = breed.referenceImageId ?: return@launch)
            val result = favouritesService.addFavourite(fav)
            state.value = when {
                result.isSuccess -> getFavouriteList()
                else -> FavouritesUiState(error = "Failed to add favorite")
            }
        }
    }

    //remove item to the favorites list
    fun removeFavorite(context: Context, breed: BreedsModel) {
        viewModelScope.launch {
            val fav =
                state.value.data.find { it.imageId == breed.referenceImageId } ?: return@launch

            val result = favouritesService.removeFavourite(fav.id)
            state.value = when {
                result.isSuccess -> getFavouriteList()
                else -> FavouritesUiState(error = "Failed to remove favorite")
            }
        }
    }
}