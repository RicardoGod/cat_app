package com.example.cat_app.data.services

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val servicesModule = module {
    singleOf(::BreedsService) bind IBreedsService::class
    singleOf(::FavouritesService) bind IFavouritesService::class
}