package com.example.cat_app.ui

import com.example.cat_app.ui.features.breeds.BreedsViewModel
import com.example.cat_app.ui.features.favourites.FavouritesViewModel
import com.example.cat_app.viewmodel.SearchViewModel
import org.koin.dsl.module

val components = module {
    single { BreedsViewModel(get()) }
    single { FavouritesViewModel(get()) }
    single { SearchViewModel() }
}