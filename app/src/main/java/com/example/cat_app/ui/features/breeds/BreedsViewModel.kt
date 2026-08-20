package com.example.cat_app.ui.features.breeds

import android.util.Log
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
            is BreedsEvent.SearchChanged -> {
                state.value = state.value.copy(search = event.value)
                searchBreeds(event.value)
            }
//                searchBreeds(event.text)
            is BreedsEvent.ClearSearch -> clearSearch()
            is BreedsEvent.ToggleFavorite -> toggleFavourite(event.breed)
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

    private fun searchBreeds(query: String) {
        viewModelScope.launch {

            val newState = useCases.searchBreeds(
                state = state.value.copy(search = query),
                query = query
            )
            state.value = newState
//            state.compareAndSet(
//                state.value,
//                useCases.searchBreeds(state = state.value, query = query)
//            )
        }

    }
    private fun clearSearch() {
        viewModelScope.launch {
            state.value = state.value.copy(
                search = "",
                breeds = state.value.breeds,
                error = null
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