package com.example.cat_app.ui.features.breeds

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class BreedsViewModel(private val useCases: BreedsUseCases) : ViewModel() {
    //Mutable Search - only accessible within this class
    private val _searchQuery = MutableStateFlow("")

    //Read Only Search
    val searchQuery = _searchQuery.asStateFlow()

    //Mutable State - only accessible within this class
    private val _state = MutableStateFlow(BreedsUiState())

    //Read Only State
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            searchQuery
                .drop(1) // this will prevent that init call performs fetchBreeds and that responsibility will be on the event of Loading
                .debounce(300.milliseconds)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        searchBreeds(query)
                    } else {
                        fetchBreeds()
                    }
                }
        }
    }

    fun onEvent(event: BreedsEvent){
        when(event){
            is BreedsEvent.LoadingScreen -> fetchBreeds()
            is BreedsEvent.BreedClicked -> selectBreed(event.breed)
            is BreedsEvent.CloseDialog -> unselectBreed()
            is BreedsEvent.SearchChanged -> updateSearchQueryValue(event.value)
            is BreedsEvent.ToggleFavorite -> toggleFavourite(event.breed)
            is BreedsEvent.ClearSearch -> updateSearchQueryValue("")
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            val newState = useCases.fetchBreeds(state = state.value)
            _state.value = newState
        }
    }

    private fun searchBreeds(query: String) {
        viewModelScope.launch {

            val newState = useCases.searchBreeds(
                state = state.value.copy(search = query),
                query = query
            )

            _state.value = newState
        }

    }

    private fun updateSearchQueryValue(query: String) {
        _searchQuery.update({ query })
    }

    private fun toggleFavourite(breed: BreedUi) {
        viewModelScope.launch {
            val newState = useCases.toggleFavourite(
                state = state.value,
                id = breed.id
            )

            _state.update { newState }
        }
    }

    private fun selectBreed(breed: BreedUi) {
        _state.update {
            it.copy(
                selectedBreed = breed
            )
        }
    }
    private fun unselectBreed(){
        _state.update {
            it.copy(
                selectedBreed = null
            )
        }
    }
}