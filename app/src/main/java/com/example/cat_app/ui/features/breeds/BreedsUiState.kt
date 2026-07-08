package com.example.cat_app.ui.features.breeds

import com.example.cat_app.ui.features.breeds.model.BreedUi

data class BreedsUiState(
    var breeds: List<BreedUi> = emptyList(),
    var selectedBreed: BreedUi? = null,
    var search: String = String(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pageSize: Int = 10,
    val currentPage: Int = 0
)