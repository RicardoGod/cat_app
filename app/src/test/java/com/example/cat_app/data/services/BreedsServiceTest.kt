package com.example.cat_app.data.services

import ads_mobile_sdk.va
import com.example.cat_app.data.api.CatsApiService
import com.example.cat_app.helper.FakeBreedsModel
import com.example.cat_app.helper.FakeCatDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class BreedsServiceTest {

    private val catsApi: CatsApiService = mock()

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    single { catsApi}
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun getBreedsList_returnsMappedBreeds() = runTest {

        val dto = FakeCatDto.persian

        whenever(catsApi.getCatsList(10, 0))
            .thenReturn(Response.success(listOf(dto)))

        val service = BreedsService()

        val result = service.getBreedsList(10,0)

        assertTrue(result.isSuccess)

        val expected = listOf(FakeBreedsModel.persian)
        assertEquals(
            expected,
            result.getOrThrow()
        )
    }

    @Test
    fun getBreedsList_returnsEmptyList() = runTest {

        whenever(catsApi.getCatsList(any(), any()))
            .thenReturn(Response.success((emptyList())))

        val service = BreedsService()

        val result = service.getBreedsList(10,0)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun getBreedsList_returnsFailure_whenApiThrows() = runTest {

        whenever(catsApi.getCatsList(any(), any()))
            .thenThrow(RuntimeException())

        val service = BreedsService()

        val result = service.getBreedsList(10,0)

        assertTrue(result.isFailure)
    }


    @Test
    fun searchBreeds_returnsMappedList() = runTest {

        val dto = FakeCatDto.persian

        whenever(catsApi.searchBreeds("persian",1))
            .thenReturn(Response.success(listOf(dto)))

        val service = BreedsService()
        val result = service.searchBreeds("persian")

        assertTrue(result.isSuccess)
        assertEquals(
            listOf(dto.toBreedsModel()),
            result.getOrThrow()
        )
    }

    @Test
    fun searchBreeds_returnsEmptyList() = runTest {

        whenever(catsApi.searchBreeds(any(), any()))
            .thenReturn(Response.success(emptyList()))

        val service = BreedsService()
        val result = service.searchBreeds("abc")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun searchBreeds_returnsFailure_whenApiThrows() = runTest {

        whenever(catsApi.searchBreeds(any(), any()))
            .thenThrow(RuntimeException())

        val service = BreedsService()
        val result = service.searchBreeds("persian")

        assertTrue(result.isFailure)
    }
}