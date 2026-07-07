package com.example.cat_app.ui.features.breeds

import com.example.cat_app.data.models.BreedsModel

data class BreedsUiState(
    val data: List<BreedsModel> = emptyList(),
    var search: String = String(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pageSize: Int = 10,
    val currentPage: Int = 0
)