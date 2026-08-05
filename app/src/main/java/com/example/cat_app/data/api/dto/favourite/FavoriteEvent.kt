package com.example.cat_app.data.api.dto.favourite

import com.example.cat_app.ui.features.breeds.model.BreedUi

sealed interface FavoriteEvent {
    data object LoadingScreen : FavoriteEvent
    data class BreedClicked(val breed: BreedUi) : FavoriteEvent
    data object CloseDialog : FavoriteEvent
}


