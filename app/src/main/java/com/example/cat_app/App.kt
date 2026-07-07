package com.example.cat_app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.cat_app.data.api.catsModule
import com.example.cat_app.data.services.servicesModule
import com.example.cat_app.settings.network.connectionModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this

        //initializes the Koin dependency injection framework
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    catsModule,
                    servicesModule,
                    connectionModule
                )
            )
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}