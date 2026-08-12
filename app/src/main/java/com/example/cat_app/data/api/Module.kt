package com.example.cat_app.data.api

import com.example.cat_app.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val catsModule = module {

    single<String>(qualifier = org.koin.core.qualifier.named("baseUrl")) {
        "https://api.thecatapi.com/"
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .header("x-api-key", BuildConfig.CAT_API_KEY)
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    single {
        GsonBuilder()
            .setLenient()
            .create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(get<String>(org.koin.core.qualifier.named("baseUrl")))
            .client(get())
            .addConverterFactory(
                GsonConverterFactory.create(get())
            )
            .build()
    }

    single<CatsApiService> {
        get<Retrofit>()
            .create(CatsApiService::class.java)
    }
}