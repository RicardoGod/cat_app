package com.example.cat_app.ui.features.breeds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun BreedsRoute(
    onNavigate: () -> Unit,
    viewModel: BreedsViewModel = koinViewModel()
) {
    val search by viewModel.searchQuery.collectAsState()
    val state by viewModel.state.collectAsState()

    ScreenBreeds(
        search = search,
        state = state,
        onEvent = viewModel::onEvent,
        navigateBack = onNavigate)
}