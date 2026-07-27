package com.example.cat_app.ui.features.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BreedsViewModel(private val useCases: BreedsUseCases) : ViewModel() {
    val state = MutableStateFlow(BreedsUiState())

    fun onEvent(event: BreedsEvent){
        when(event){
            is BreedsEvent.LoadingScreen -> fetchBreeds()
            is BreedsEvent.BreedClicked -> selectBreed(event.breed)
            is BreedsEvent.CloseDialog -> unselectBreed()
            is BreedsEvent.SearchChanged -> searchBreeds(event.text)
            is BreedsEvent.ToggleFavorite -> toggleFavourite(event.breed)
            else -> {}
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            state.value = useCases.fetchBreeds(state.value)
        }
    }

    private fun searchBreeds(query: String) {
        viewModelScope.launch {
            state.compareAndSet(
                state.value,
                useCases.searchBreeds(state = state.value, query = query)
            )
        }
    }

    private fun toggleFavourite(breed: BreedUi) {
        viewModelScope.launch {
            state.compareAndSet(
                state.value,
                useCases.toggleFavourite(state.value, breed.id)
            )
        }
    }

    private fun selectBreed(breed: BreedUi) {
        state.update {
            it.copy(
                selectedBreed = breed
            )
        }
    }
    private fun unselectBreed(){
        state.value.selectedBreed = null
    }
}