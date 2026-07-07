package com.example.cat_app.ui.features.favourites

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesRoute(
    viewModel: FavouritesViewModel = koinViewModel()
) {
    ScreenFavourites(viewModel.state, viewModel::onEvent)
}