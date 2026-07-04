package com.example.cat_app.data.services

import com.example.cat_app.data.models.BreedsModel

interface IBreedsService {

    suspend fun getBreedsList(limit: Int, page: Int): Result<List<BreedsModel>>
    suspend fun searchBreeds(query: String, attachImage: Int = 1): Result<List<BreedsModel>>

}
