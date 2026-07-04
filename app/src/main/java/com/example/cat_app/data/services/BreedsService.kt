package com.example.cat_app.data.services

import com.example.cat_app.data.api.CatsApiService
import com.example.cat_app.data.api.dto.cat.CatDto
import com.example.cat_app.data.models.BreedsModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/*
    Allows to have multiple external services if we want of animals
    Right now we have CatsApi integrated, but we can have DogsApi in the future, for example
 */
class BreedsService : IBreedsService, KoinComponent {
     val catsApi: CatsApiService by inject()

    override suspend fun getBreedsList(limit: Int,page: Int): Result<List<BreedsModel>> {
        try{
            val catsApiResponse = catsApi.getCatsList(limit, page).body()
            val response =  catsApiResponse?.map(CatDto::toBreedsModel).orEmpty()
            return Result.success(response)
        } catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun searchBreeds(query: String, attachImage: Int): Result<List<BreedsModel>> {
      try {
          val catsApiResponse = catsApi.searchBreeds(query, attachImage).body()
          val response =  catsApiResponse?.map(CatDto::toBreedsModel).orEmpty()
          return Result.success(response)
      } catch (e: Exception) {
          return Result.failure(e)
      }
    }
}