package com.example.cat_app.ui.features.breeds

import android.text.format.DateUtils
import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.models.FavouriteModel
import com.example.cat_app.data.models.FavouriteRequestModel
import com.example.cat_app.data.services.BreedsService
import com.example.cat_app.data.services.FavouritesService
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.data.services.IFavouritesService
import com.example.cat_app.helper.FakeBreedsModel
import com.example.cat_app.helper.FakeBreedsUi
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import com.google.crypto.tink.shaded.protobuf.LazyStringArrayList.emptyList
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.refEq
import org.mockito.kotlin.whenever
import java.time.Instant.now
import java.util.Date
import kotlin.collections.emptyList
import kotlin.random.Random

class BreedsUseCasesKoinTest {

    private val breedService: IBreedsService = mock()
    private val favoriteService: IFavouritesService = mock()

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    single { breedService}
                    single { favoriteService}
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun fetchBreeds_returnsExpectedState() = runTest {

        val expected = FakeBreedsModel.persian

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(listOf(expected)))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        val useCase = BreedsUseCases()

        val result = useCase.fetchBreeds(BreedsUiState())

        val expectedBreedsList = listOf(BreedUi.fromBreedsModel(expected, false))

        assertEquals(
            BreedsUiState(breeds = expectedBreedsList),
            result
        )
    }

    @Test
    fun fetchBreeds_marksFavouriteBreeds() = runTest {

        val persian = FakeBreedsModel.persian

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(listOf(persian)))

        whenever(favoriteService.getFavourites())
            .thenReturn(
                Result.success(
                    listOf(
                        FavouriteModel(
                            id = Random.nextInt(),
                            imageId = persian.image?.id ?: "",
                            createdAt = now().toString()
                        )
                    )
                )
            )

        val result = BreedsUseCases().fetchBreeds(BreedsUiState())

        val expected = BreedUi.fromBreedsModel(
            persian,
            isFavorite = true
        )

        assertEquals(listOf(expected), result.breeds)
    }

    @Test
    fun fetchBreeds_returnsEmptyList_whenServiceReturnsEmpty() = runTest {

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(emptyList<BreedsModel>()))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        val result = BreedsUseCases().fetchBreeds(BreedsUiState())

        assertTrue(result.breeds.isEmpty())
    }

    @Test
    fun fetchBreeds_returnsError_whenBreedServiceFails() = runTest {

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.failure(Exception()))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        val result = BreedsUseCases().fetchBreeds(BreedsUiState())


        assertTrue(result.error != null)
    }

    @Test
    fun fetchBreeds_returnsBreeds_whenFavouriteServiceFails() = runTest {

        val persian = FakeBreedsModel.persian

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(listOf(persian)))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.failure(Exception()))

        val result = BreedsUseCases().fetchBreeds(BreedsUiState())

        val expected = BreedUi.fromBreedsModel(
            persian,
            false
        )

        assertEquals(listOf(expected), result.breeds)
    }

    @Test
    fun fetchBreeds_keepsSelectedBreed() = runTest {

        val selected = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            false
        )

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(emptyList<BreedsModel>()))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        val result = BreedsUseCases().fetchBreeds(
            BreedsUiState(
                selectedBreed = selected
            )
        )

        assertEquals(selected, result.selectedBreed)
    }

    @Test
    fun fetchBreeds_marksOnlyFavouriteBreed() = runTest {

        val persian = FakeBreedsModel.persian
        val bengal = FakeBreedsModel.bengal

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(listOf(persian, bengal)))

        whenever(favoriteService.getFavourites())
            .thenReturn(
                Result.success(
                    listOf(
                        FavouriteModel(
                            id = Random.nextInt(),
                            imageId = bengal.image?.id ?: "",
                            createdAt = now().toString()
                        )
                    )
                )
            )

        val result = BreedsUseCases().fetchBreeds(BreedsUiState())

        assertFalse(result.breeds[0].isFavorite)
        assertTrue(result.breeds[1].isFavorite)
    }
}