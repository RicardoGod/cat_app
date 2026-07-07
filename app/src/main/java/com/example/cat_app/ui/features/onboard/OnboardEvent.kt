package com.example.cat_app.ui.features.onboard

import com.example.cat_app.data.models.BreedsModel

sealed interface OnboardEvent {
    data object NavigateToBreeds: OnboardEvent
    data object NavigateToFavorites: OnboardEvent
}