package com.example.cat_app.ui.features.breeds

sealed interface BreedsEvent {

    data class SearchChanged(
        val text: String
    ) : BreedsEvent

    data class BreedClicked(
        val breedId: String
    ) : BreedsEvent

    data class ToggleFavorite(
        val breedId: String
    ) : BreedsEvent

    data object CloseDialog : BreedsEvent
}