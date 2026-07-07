package com.example.cat_app.settings.network

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val connectionModule = module {
    singleOf(::ConnectionService)
}