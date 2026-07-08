package com.example.cat_app.ui.features.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashRoute(onEvent: (SplashEvent) -> Unit) {
    ScreenSplash(onEvent)
}