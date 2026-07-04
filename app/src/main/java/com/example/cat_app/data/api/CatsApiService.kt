package com.example.cat_app.data.api

import com.example.cat_app.data.api.dto.cat.CatDto
import com.example.cat_app.data.api.dto.favourite.FavouriteDto
import com.example.cat_app.data.api.dto.favourite.FavouriteRequest
import org.koin.core.component.KoinComponent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApiService: KoinComponent{

    suspend fun getCatsList(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<List<CatDto>>

    // GET - favorite list
    @GET("v1/favourites")
    suspend fun getFavorites(): Response<List<FavouriteDto>>

    // POST - add favorites
    @POST("v1/favourites")
    suspend fun addFavourite(
        @Body request: FavouriteRequest
    ): Response<FavouriteDto>

    // DELETE - Remove favorites
    @DELETE("v1/favourites/{favourite_id}")
    suspend fun removeFavourite(
        @Path("favourite_id") favouriteId: Int
    ): Response<Unit>

    // GET - breed search
    @GET("v1/breeds/search")
    suspend fun searchBreeds(
        @Query("q") query: String,
        @Query("attach_image") attachImage: Int = 1
    ): Response<List<CatDto>>
}