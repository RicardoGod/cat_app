package com.example.cat_app.data.services

import com.example.cat_app.data.api.CatsApiService
import com.example.cat_app.data.api.dto.favourite.FavouriteDto
import com.example.cat_app.data.api.dto.favourite.FavouriteRequest
import com.example.cat_app.data.models.FavouriteModel
import com.example.cat_app.data.models.FavouriteRequestModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavouritesService : IFavouritesService, KoinComponent {
    val catsApi: CatsApiService by inject()

    override suspend fun getFavorites(): Result<List<FavouriteModel>> {
        try {
            val catsApiResponse = catsApi . getFavorites ().body()
            val response = catsApiResponse?.map(FavouriteDto::toFavouriteModel)
            return Result.success(response.orEmpty())
        }catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun addFavourite(request: FavouriteRequestModel): Result<FavouriteModel?> {
        try {
            val catsApiResponse = catsApi.addFavourite(FavouriteRequest.Factory.fromFavouriteRequest(request)).body()
            val response =  catsApiResponse?.toFavouriteModel()
            return Result.success(response)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun removeFavourite(favouriteId: Int): Result<Unit?> {
        try {
            val catsApiResponse = catsApi.removeFavourite(favouriteId).body()
            return Result.success(catsApiResponse)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}