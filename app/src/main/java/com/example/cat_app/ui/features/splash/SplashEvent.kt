package com.example.cat_app.ui.features.splash

sealed interface SplashEvent {
    data object Loading: SplashEvent
}