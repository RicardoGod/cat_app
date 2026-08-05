package com.example.cat_app.data.api.dto.favourite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_app.ui.features.breeds.BreedsUseCases
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FavoriteViewModel(private val useCases: BreedsUseCases) : ViewModel() {
    val state = MutableStateFlow(BreedsUiState())

    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.LoadingScreen -> fetchBreeds()
            is FavoriteEvent.BreedClicked -> selectBreed(event.breed)
            else -> {}
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {

            Log.d("BreedsViewModel", "fetchBreeds() chamada. Estado atual: ${state.value}")
            val newState = useCases.fetchBreeds(state.value)

            Log.d("BreedsViewModel", "fetchBreeds() terminou. Novo estado: $newState")
            state.value = useCases.fetchBreeds(state.value)
        }
    }

    private fun selectBreed(breed: BreedUi) {
        state.update {
            it.copy(
                selectedBreed = breed
            )
        }
    }

}