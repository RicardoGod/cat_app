package com.example.cat_app.ui.features.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OnboardViewModel(useCase: OnboardUseCases) : ViewModel() {
    private val _navigation =
        MutableSharedFlow<OnboardEvent>()

    val navigation =
        _navigation.asSharedFlow()

    fun onEvent(event: OnboardEvent): Unit {
        when (event) {
            is OnboardEvent.NavigateToBreeds ->
                viewModelScope.launch {
                    _navigation.emit(OnboardEvent.NavigateToBreeds)
                }

            is OnboardEvent.NavigateToFavorites ->
                viewModelScope.launch {
                    _navigation.emit(OnboardEvent.NavigateToFavorites)
                }
        }
    }
}