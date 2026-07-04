package com.example.cat_app.ui.components.events

import com.example.cat_app.data.models.BreedsModel

sealed interface BreedsEvent {

    data class SearchChanged(
        val text: String
    ) : BreedsEvent

    data class DeleteBreed(
        val breed: BreedsModel
    ) : BreedsEvent

    data object Refresh : BreedsEvent
}