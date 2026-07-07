package com.example.cat_app.ui.features.onboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardRoute(
    viewModel: OnboardViewModel = koinViewModel(),
    onNavigate: (OnboardEvent) -> Unit
) {
    ScreenOnboard(viewModel = viewModel)

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            onNavigate(event)
        }
    }
}