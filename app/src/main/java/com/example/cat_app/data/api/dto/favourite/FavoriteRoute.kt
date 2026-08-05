package com.example.cat_app.data.api.dto.favourite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.cat_app.ui.features.breeds.BreedsViewModel
import com.example.cat_app.ui.features.breeds.ScreenBreeds
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteRoute(
    onNavigate: () -> Unit,
    viewModel: FavoriteViewModel = koinViewModel()
) {
    ScreenFavorite(
        state = viewModel.state.collectAsState().value,
        onEvent = viewModel::onEvent,
        navigateBack = onNavigate)
}