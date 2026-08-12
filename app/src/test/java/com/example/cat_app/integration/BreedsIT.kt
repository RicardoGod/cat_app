package com.example.cat_app.integration

import com.example.cat_app.data.api.CatsApiService
import com.example.cat_app.data.services.servicesModule
import com.example.cat_app.ui.components
import com.example.cat_app.ui.features.breeds.BreedsUseCases
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import com.google.gson.GsonBuilder
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import junit.framework.TestCase.assertEquals

class BreedsIT {

    private lateinit var server: MockWebServer

    private val testApiModule = module {

        single<String>(named("baseUrl")) {
            server.url("/").toString()
        }

        single {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .header("x-api-key", "test-api-key")
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
                .baseUrl(get<String>(named("baseUrl")))
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

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        startKoin {
            modules(
                testApiModule,
                servicesModule,
                components
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        server.shutdown()
    }

    @Test
    fun fetchBreeds_mapsApiResponseCorrectly() = runTest {

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    [
                        {
                            "id": "abys",
                            "name": "Abyssinian",
                            "origin": "Egypt",
                            "temperament": "Active",
                            "description": "Very active cat",
                            "life_span": "14 - 15",
                            "weight": {
                                "imperial": "7 - 10",
                                "metric": "3 - 5"
                            },
                            "image": {
                                "id": "img1",
                                "url": "https://cat.com/cat.jpg",
                                "width": 500,
                                "height": 500
                            }
                        }
                    ]
                    """.trimIndent()
                )
        )

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("[]")
        )

        val result = BreedsUseCases()
            .fetchBreeds(BreedsUiState())

        assertEquals(1, result.breeds.size)

        val breed = result.breeds.first()

        assertEquals("abys", breed.id)
        assertEquals("Abyssinian", breed.name)
        assertEquals("Egypt", breed.origin)
        assertEquals(false, breed.isFavorite)

        assertEquals(2, server.requestCount)

        val breedsRequest = server.takeRequest()
        assertEquals("/v1/breeds", breedsRequest.path?.substringBefore("?"))

        val favouritesRequest = server.takeRequest()
        assertEquals("/v1/favourites", favouritesRequest.path)
    }
}