package com.example.cat_app.ui.features.breeds

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.ui.features.breeds.model.BreedUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BreedsViewModel : ViewModel() {
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
            state.compareAndSet(
                state.value,
                BreedsUseCases().fetchBreeds(state.value)
            )
        }
    }

    private fun searchBreeds(query: String) {
        viewModelScope.launch {
            state.compareAndSet(
                state.value,
                BreedsUseCases().searchBreeds(state = state.value, query = query)
            )
        }
    }

    private fun toggleFavourite(breed: BreedUi) {
        viewModelScope.launch {
            state.compareAndSet(
                state.value,
                BreedsUseCases().toggleFavourite(state.value, breed.id)
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

    fun getBreedImageUrl(breed: BreedsModel): String {
        return breed.image?.url
            ?: breed.referenceImageId?.let { "https://cdn2.thecatapi.com/images/$it.jpg" }
            ?: "https://cdn2.thecatapi.com/images/default.jpg"
    }

    fun clearSearch() {
        state.value.search = String()
    }
}