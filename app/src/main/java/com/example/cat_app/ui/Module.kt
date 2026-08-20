package com.example.cat_app.ui

import com.example.cat_app.data.api.dto.favourite.FavoriteViewModel
import com.example.cat_app.data.services.BreedsService
import com.example.cat_app.data.services.FavouritesService
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.data.services.IFavouritesService
import com.example.cat_app.ui.features.breeds.BreedsUseCases
import com.example.cat_app.ui.features.breeds.BreedsViewModel
import com.example.cat_app.ui.features.onboard.OnboardViewModel
import org.koin.dsl.module

val components = module {
    single { BreedsViewModel(get()) }
    single { FavoriteViewModel(get()) }
    single { OnboardViewModel() }


    single<IBreedsService> { BreedsService() }
    single<IFavouritesService> { FavouritesService() }

    single { BreedsUseCases() }
}

