package com.example.cat_app.data.services

import com.example.cat_app.data.models.FavouriteModel
import com.example.cat_app.data.models.FavouriteRequestModel

interface IFavouritesService {

    suspend fun getFavourites(): Result<List<FavouriteModel>>
    suspend fun addFavourite(request: FavouriteRequestModel): Result<FavouriteModel?>
    suspend fun removeFavourite(favouriteId: Int): Result<Unit?>
}