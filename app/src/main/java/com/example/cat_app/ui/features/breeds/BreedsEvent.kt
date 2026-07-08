package com.example.cat_app.ui.features.breeds

import com.example.cat_app.ui.features.breeds.model.BreedUi

sealed interface BreedsEvent {

    data class SearchChanged(val text: String) : BreedsEvent

    data object ClearSearch : BreedsEvent

    data class BreedClicked(val breed: BreedUi) : BreedsEvent

    data class ToggleFavorite(val breed: BreedUi) : BreedsEvent

    data object LoadingScreen : BreedsEvent

    data object CloseDialog : BreedsEvent
}