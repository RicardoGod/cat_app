package com.example.cat_app.ui.features.breeds

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
            is BreedsEvent.SearchChanged -> updateSearchQueryValue(event.text)
            is BreedsEvent.ToggleFavorite -> toggleFavourite(event.breed)
            is BreedsEvent.ClearSearch -> updateSearchQueryValue("")
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val breeds = useCases.fetchBreeds(state.value.pageSize, state.value.currentPage)
            _state.update {
                if (breeds != null) {
                    it.copy(breeds = breeds, error = null, isLoading = false)
                } else {
                    it.copy(error = "Error fetching breeds", isLoading = false)
                }
            }
        }
    }

    private fun searchBreeds(query: String) {

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val breeds = useCases.searchBreeds(query = query)
            _state.update {
                if (breeds != null) {
                    it.copy(breeds = breeds, error = null, isLoading = false)
                } else {
                    it.copy(error = "Error fetching breeds", isLoading = false)
                }
            }
        }
    }

    private fun updateSearchQueryValue(query: String) {
        _searchQuery.update({ query })
    }

    private fun toggleFavourite(breed: BreedUi) {
        viewModelScope.launch {
            val updatedBreedsList = useCases.toggleFavourite(
                breeds = state.value.breeds,
                id = breed.id
            )

            _state.update {
                it.copy(breeds = updatedBreedsList)
            }
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