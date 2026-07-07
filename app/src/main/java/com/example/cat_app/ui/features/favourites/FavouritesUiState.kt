package com.example.cat_app.ui.features.favourites

import com.example.cat_app.data.models.FavouriteModel

data class FavouritesUiState(
    val data: List<FavouriteModel> = emptyList(),
    val selected: FavouriteModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)