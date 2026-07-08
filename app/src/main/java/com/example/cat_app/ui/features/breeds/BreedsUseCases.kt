package com.example.cat_app.ui.features.breeds

import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.models.FavouriteModel
import com.example.cat_app.data.models.FavouriteRequestModel
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.data.services.IFavouritesService
import com.example.cat_app.ui.features.breeds.model.BreedUi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class BreedsUseCases : KoinComponent{

    val breedsService: IBreedsService by inject()
    val favouriteService: IFavouritesService by inject()

    suspend fun fetchBreeds(state: BreedsUiState): BreedsUiState {
        val breeds = breedsService.getBreedsList(
            limit = state.pageSize,
            page = state.currentPage
        )

        val favourites = favouriteService.getFavourites()

        when {
            breeds.isSuccess -> state.breeds = breeds.getOrThrow()
                .map { breed -> BreedUi.fromBreedsModel(breed, isBreedFavourite(breed, favourites.getOrThrow())) }
            else -> BreedsUiState(error = "Failed to load breeds")
        }
        return state
    }


    private fun isBreedFavourite(breed: BreedsModel, favourites: List<FavouriteModel>): Boolean {
        return favourites.any { fav -> fav.imageId == breed.image?.id }
    }

    suspend fun searchBreeds(state: BreedsUiState, query: String): BreedsUiState {
        val breeds = breedsService.searchBreeds(query)
        val favourites = favouriteService.getFavourites()

        when {
            breeds.isSuccess -> state.breeds = breeds.getOrThrow()
                .map { breed -> BreedUi.fromBreedsModel(breed, isBreedFavourite(breed, favourites.getOrThrow())) }

            else -> BreedsUiState(error = "Failed to load breeds")
        }
        return state
    }

    suspend fun toggleFavourite(state: BreedsUiState, id: String): BreedsUiState  {
        val request = FavouriteRequestModel(id)
        favouriteService.addFavourite(request)

        state.breeds
            .find { breedUi -> breedUi.id == id }
            ?.let { breedUi -> breedUi.isFavorite = true }

        return state
    }
}