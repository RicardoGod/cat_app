package com.example.cat_app.viewmodel.state

import com.example.cat_app.data.models.BreedsModel

data class BreedsUiState(
    val data: List<BreedsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pageSize: Int = 10,
    val currentPage: Int = 0
)