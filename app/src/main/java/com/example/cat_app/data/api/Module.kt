package com.example.cat_app.data.api

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val catsModule = module {
    singleOf(::CatsClient) bind CatsApiService::class
}