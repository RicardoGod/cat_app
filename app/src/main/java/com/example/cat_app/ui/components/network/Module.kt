package com.example.cat_app.ui.components.network

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val connectionModule = module {
    singleOf(::ConnectionService)
}