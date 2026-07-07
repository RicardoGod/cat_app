package com.example.cat_app.ui.features.breeds

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun BreedsRoute(
    viewModel: BreedsViewModel = koinViewModel()
) {
    ScreenBreeds(viewModel.state, viewModel.onEvent)
}