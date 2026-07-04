package com.example.cat_app.viewmodel

import org.koin.dsl.module

val components = module {
    single { BreedsViewModel(get())}
    single { FavouritesViewModel(get()) }
    single { SearchViewModel()  }
}