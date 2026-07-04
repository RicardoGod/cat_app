package com.example.cat_app.ui.components.navigate

import androidx.compose.runtime.Composable
import com.example.cat_app.ui.screen.ScreenBreeds
import com.example.cat_app.viewmodel.BreedsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BreedsRoute(
    viewModel: BreedsViewModel = koinViewModel()
) {
    ScreenBreeds(viewModel.state, viewModel::onEvent)
}