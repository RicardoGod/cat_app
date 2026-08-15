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

    suspend fun fetchBreeds(pageSize: Int, currentPage: Int): List<BreedUi>? {
        val breeds = breedsService.getBreedsList(
            limit = pageSize,
            page = currentPage
        )

        val favourites = favouriteService.getFavourites()

        return when {
            breeds.isSuccess -> buildBreedUiStateFromCurrentState(breeds, favourites)
            else -> null
        }
    }


    private fun isBreedFavourite(breed: BreedsModel, favourites: List<FavouriteModel>?): Boolean {
        return favourites?.any { fav -> fav.imageId == breed.image?.id } ?: false
    }

    suspend fun searchBreeds(query: String): List<BreedUi>? {
        val breeds = breedsService.searchBreeds(query)
        val favourites = favouriteService.getFavourites()

        return when {
            breeds.isSuccess -> buildBreedUiStateFromCurrentState(breeds, favourites)
            else -> null
        }
    }

    private fun buildBreedUiStateFromCurrentState(
        breeds: Result<List<BreedsModel>>,
        favourites: Result<List<FavouriteModel>>
    ): List<BreedUi> {
        return breeds.getOrThrow()
            .map {
                BreedUi.fromBreedsModel(
                    it,
                    isBreedFavourite(it, favourites.getOrNull())
                )
            }
    }

    suspend fun toggleFavourite(breeds: List<BreedUi>, id: String): List<BreedUi> {

        val breed = breeds
            .find { it.id == id }
            ?: return breeds

        val updatedIsFavorite =
            if (breed.isFavorite) {
                removeFavourite(id)
            } else {
                addFavourite(id)
            }

        return breeds.map { breed ->
                if (breed.id == id) {
                    breed.copy(isFavorite = updatedIsFavorite)
                }
                else {
                    breed
                }
            }

    }

    suspend fun addFavourite(id: String): Boolean{
        val request = FavouriteRequestModel(id)
        val result = favouriteService.addFavourite(request)

        return if (result.isSuccess) {
            true
        } else {
            false
        }
    }

    suspend fun removeFavourite(id: String): Boolean{
        val result = favouriteService.removeFavourite(id)

        return if (result.isSuccess) {
            false
        } else {
            true
        }
    }
}