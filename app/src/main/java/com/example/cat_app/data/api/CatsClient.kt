package com.example.cat_app.data.api

import com.example.cat_app.data.api.dto.cat.CatDto
import com.example.cat_app.data.api.dto.favourite.FavouriteDto
import com.example.cat_app.data.api.dto.favourite.FavouriteRequest
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatsClient: CatsApiService {

    private val apiKey = "live_u2drf7PKRdtYsYO55nXCH7t9TaSb2WsIZ5cO2PDYezRh08RPzt9yEVtrOTi3dChV"

    val api: CatsApiService

        get() {

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("x-api-key", apiKey)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(CatsApiService::class.java)
        }

    override suspend fun getCatsList(
        limit: Int,
        page: Int
    ): Response<List<CatDto>> {
        return api.getCatsList(limit, page)
    }

    override suspend fun getFavorites(): Response<List<FavouriteDto>> {
        return api.getFavorites()
    }

    override suspend fun addFavourite(request: FavouriteRequest): Response<FavouriteDto> {
        return api.addFavourite(request)
    }

    override suspend fun removeFavourite(favouriteId: Int): Response<Unit> {
        return api.removeFavourite(favouriteId)
    }

    override suspend fun searchBreeds(
        query: String,
        attachImage: Int
    ): Response<List<CatDto>> {
        return api.searchBreeds(query, attachImage)
    }
}