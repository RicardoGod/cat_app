package com.example.cat_app.ui.features.breeds

import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.models.FavouriteModel
import com.example.cat_app.data.models.FavouriteRequestModel
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.data.services.IFavouritesService
import com.example.cat_app.helper.FakeBreedsModel
import com.example.cat_app.helper.FakeBreedsUi
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
import org.mockito.kotlin.never
import org.mockito.kotlin.refEq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant.now
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
        //Given
        val expected = FakeBreedsModel.persian

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(listOf(expected)))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        //When
        val useCase = BreedsUseCases()
        val result = useCase.fetchBreeds(BreedsUiState())

        //Assert
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

    @Test
    fun toggleFavourite_addsFavourite_whenBreedIsNotFavourite() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = false
        )

        val state = BreedsUiState(
            breeds = listOf(breed)
        )

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.success(null))

        val result = BreedsUseCases().toggleFavourite(
            state,
            breed.id
        )

        assertTrue(result.breeds.first().isFavorite)
    }

    @Test
    fun toggleFavourite_removesFavourite_whenBreedIsFavourite() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        val state = BreedsUiState(
            breeds = listOf(breed)
        )

        whenever(
            favoriteService.removeFavourite(breed.id)
        ).thenReturn(
            Result.success(Unit)
        )

        val result = BreedsUseCases().toggleFavourite(
            state,
            breed.id
        )

        assertFalse(
            result.breeds.first().isFavorite
        )

        verify(favoriteService).removeFavourite(breed.id)
    }

    @Test
    fun toggleFavourite_doesNotAdd_whenBreedIsFavourite() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        whenever(
            favoriteService.removeFavourite(breed.id)
        ).thenReturn(
            Result.success(Unit)
        )

        BreedsUseCases().toggleFavourite(
            BreedsUiState(
                breeds = listOf(breed)
            ),
            breed.id
        )

        verify(favoriteService).removeFavourite(breed.id)
        verify(favoriteService, org.mockito.kotlin.never())
            .addFavourite(any())
    }

    @Test
    fun toggleFavourite_keepsFavourite_whenRemoveFails() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        val state = BreedsUiState(
            breeds = listOf(breed)
        )

        whenever(
            favoriteService.removeFavourite(breed.id)
        ).thenReturn(
            Result.failure(Exception())
        )

        val result = BreedsUseCases().toggleFavourite(
            state,
            breed.id
        )

        assertTrue(
            result.breeds.first().isFavorite
        )
    }

    @Test
    fun toggleFavourite_callsFavouriteServiceWithCorrectId() = runTest {

        val breed = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            false
        )

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.success(null))

        BreedsUseCases().toggleFavourite(
            BreedsUiState(
                breeds = listOf(breed)
            ),
            breed.id
        )

        verify(favoriteService)
            .addFavourite(
                refEq(FavouriteRequestModel(breed.id))
            )
    }

    @Test
    fun toggleFavourite_keepsOtherBreedsUnchanged() = runTest {

        val persian = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            false
        )

        val bengal = BreedUi.fromBreedsModel(
            FakeBreedsModel.bengal,
            false
        )

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.success(null))

        val result = BreedsUseCases().toggleFavourite(
            BreedsUiState(
                breeds = listOf(
                    persian,
                    bengal
                )
            ),
            persian.id
        )

        assertTrue(result.breeds[0].isFavorite)
        assertFalse(result.breeds[1].isFavorite)
    }

    @Test
    fun toggleFavourite_returnsSameStateInstance_whenServiceFails() = runTest {
        val breed = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            false
        )

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.failure(Exception()))

        val state = BreedsUiState(
            breeds = listOf(breed)
        )

        val result = BreedsUseCases().toggleFavourite(
            state,
            breed.id
        )

        assertEquals(state, result)
    }

    @Test
    fun toggleFavourite_unknownBreed_doesNothing() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = false
        )

        val state = BreedsUiState(
            breeds = listOf(breed)
        )

        val result = BreedsUseCases().toggleFavourite(
            state,
            "unknown-id"
        )

        assertEquals(state, result)

        verify(favoriteService, never())
            .addFavourite(any())

        verify(favoriteService, never())
            .removeFavourite(any())
    }

    @Test
    fun addFavourite_returnsTrue_whenServiceSucceeds() = runTest {

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.success(null))

        val result = BreedsUseCases()
            .addFavourite(FakeBreedsUi.persian.id)

        assertTrue(result)
    }

    @Test
    fun addFavourite_returnsFalse_whenServiceFails() = runTest {

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.failure(Exception()))

        val result = BreedsUseCases()
            .addFavourite(FakeBreedsUi.persian.id)

        assertFalse(result)
    }

    @Test
    fun removeFavourite_returnsFalse_whenServiceSucceeds() = runTest {

        whenever(
            favoriteService.removeFavourite(FakeBreedsUi.persian.id)
        ).thenReturn(Result.success(Unit))

        val result = BreedsUseCases()
            .removeFavourite(FakeBreedsUi.persian.id)

        assertFalse(result)
    }

    @Test
    fun removeFavourite_returnsTrue_whenServiceFails() = runTest {

        whenever(
            favoriteService.removeFavourite(FakeBreedsUi.persian.id)
        ).thenReturn(Result.failure(Exception()))

        val result = BreedsUseCases()
            .removeFavourite(FakeBreedsUi.persian.id)

        assertTrue(result)
    }

    @Test
    fun toggleFavourite_whenServiceFails_shouldNotMarkFavourite() = runTest {

        val breed = BreedUi.fromBreedsModel(
            FakeBreedsModel.persian,
            false
        )

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.failure(Exception()))

        val result = BreedsUseCases().toggleFavourite(
            BreedsUiState(
                breeds = listOf(breed)
            ),
            breed.id
        )

        assertFalse(result.breeds.first().isFavorite)
    }

    @Test
    fun searchBreeds_returnsExpectedBreeds() = runTest {

        whenever(
            breedService.searchBreeds("pers")
        ).thenReturn(
            Result.success(
                listOf(FakeBreedsModel.persian)
            )
        )

        whenever(
            favoriteService.getFavourites()
        ).thenReturn(
            Result.success(emptyList<FavouriteModel>())
        )

        val result = BreedsUseCases().searchBreeds(
            BreedsUiState(),
            "pers"
        )

        assertEquals(
            1,
            result.breeds.size
        )

        assertEquals(
            FakeBreedsModel.persian.id,
            result.breeds.first().id
        )
    }

    @Test
    fun searchBreeds_returnsEmptyList() = runTest {

        whenever(
            breedService.searchBreeds(any(), any())
        ).thenReturn(
            Result.success(emptyList<BreedsModel>())
        )

        whenever(
            favoriteService.getFavourites()
        ).thenReturn(
            Result.success(emptyList<FavouriteModel>())
        )

        val result = BreedsUseCases().searchBreeds(
            BreedsUiState(),
            "xxxx"
        )

        assertTrue(result.breeds.isEmpty())
    }

    @Test
    fun searchBreeds_returnsErrorWhenServiceFails() = runTest {

        whenever(
            breedService.searchBreeds(any(), any())
        ).thenReturn(
            Result.failure(Exception())
        )

        whenever(
            favoriteService.getFavourites()
        ).thenReturn(
            Result.success(emptyList<FavouriteModel>())
        )

        val result = BreedsUseCases().searchBreeds(
            BreedsUiState(),
            "pers"
        )

        assertTrue(result.error != null)
    }


}