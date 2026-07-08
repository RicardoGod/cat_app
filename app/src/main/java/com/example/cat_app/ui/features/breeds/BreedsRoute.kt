package com.example.cat_app.ui.features.breeds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel

@Composable
fun BreedsRoute(
    onNavigate: () -> Unit,
    viewModel: BreedsViewModel = koinViewModel()
) {
    ScreenBreeds(
        state = viewModel.state.collectAsState().value,
        onEvent = viewModel::onEvent,
        navigateBack = onNavigate)
}