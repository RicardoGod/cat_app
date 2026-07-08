package com.example.cat_app.ui

import com.example.cat_app.ui.features.breeds.BreedsViewModel
import com.example.cat_app.ui.features.onboard.OnboardViewModel
import org.koin.dsl.module

val components = module {
    single { BreedsViewModel() }
    single { OnboardViewModel() }
}