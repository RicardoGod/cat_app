package com.example.cat_app.ui.features.breeds

import com.example.cat_app.data.models.BreedsModel
import com.example.cat_app.data.models.FavouriteModel
import com.example.cat_app.data.models.FavouriteRequestModel
import com.example.cat_app.data.services.IBreedsService
import com.example.cat_app.data.services.IFavouritesService
import com.example.cat_app.helper.FakeBreedsModel
import com.example.cat_app.helper.FakeBreedsUi
import com.example.cat_app.ui.features.breeds.model.BreedUi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNull
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
        val result = useCase.fetchBreeds(0, 0)

        //Assert
        val expectedBreedsList = listOf(BreedUi.fromBreedsModel(expected, false))
        assertEquals(
            expectedBreedsList,
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

        val result = BreedsUseCases().fetchBreeds(0,0)

        val expected = BreedUi.fromBreedsModel(
            persian,
            isFavorite = true
        )

        assertEquals(listOf(expected), result)
    }

    @Test
    fun fetchBreeds_returnsEmptyList_whenServiceReturnsEmpty() = runTest {

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(emptyList<BreedsModel>()))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        val result = BreedsUseCases().fetchBreeds(0,0)

        assertTrue(result?.size == 0)
    }

    @Test
    fun fetchBreeds_returnsNull_whenBreedServiceFails() = runTest {

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.failure(Exception()))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.success(emptyList<FavouriteModel>()))

        val result = BreedsUseCases().fetchBreeds(0,0)


        assertTrue(result == null)
    }

    @Test
    fun fetchBreeds_returnsBreeds_whenFavouriteServiceFails() = runTest {

        val persian = FakeBreedsModel.persian

        whenever(breedService.getBreedsList(any(), any()))
            .thenReturn(Result.success(listOf(persian)))

        whenever(favoriteService.getFavourites())
            .thenReturn(Result.failure(Exception()))

        val result = BreedsUseCases().fetchBreeds(0,0)

        val expected = BreedUi.fromBreedsModel(
            persian,
            false
        )

        assertEquals(listOf(expected), result)
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

        val result = BreedsUseCases().fetchBreeds(0,0)

        assertEquals(false, result?.get(0)?.isFavorite)
        assertEquals(true, result?.get(1)?.isFavorite)
    }

    @Test
    fun toggleFavourite_addsFavourite_whenBreedIsNotFavourite() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = false
        )

        whenever(
            favoriteService.addFavourite(any())
        ).thenReturn(Result.success(null))

        val result = BreedsUseCases().toggleFavourite(
            breeds = listOf(breed),
            id = breed.id
        )

        assertTrue(result.first().isFavorite)
    }

    @Test
    fun toggleFavourite_removesFavourite_whenBreedIsFavourite() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        whenever(
            favoriteService.removeFavourite(breed.id)
        ).thenReturn(
            Result.success(Unit)
        )

        val result = BreedsUseCases().toggleFavourite(
            breeds = listOf(breed),
            id = breed.id
        )

        assertFalse(
            result.first().isFavorite
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
            breeds = listOf(breed),
            breed.id
        )

        verify(favoriteService).removeFavourite(breed.id)
        verify(favoriteService, never())
            .addFavourite(any())
    }

    @Test
    fun toggleFavourite_keepsFavourite_whenRemoveFails() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        whenever(
            favoriteService.removeFavourite(breed.id)
        ).thenReturn(
            Result.failure(Exception())
        )

        val result = BreedsUseCases().toggleFavourite(
            listOf(breed),
            breed.id
        )

        assertTrue(
            result.first().isFavorite
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
            breeds = listOf(breed),
            id = breed.id
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
            breeds = listOf(
                persian,
                bengal
            ),
            id = persian.id
        )

        assertTrue(result[0].isFavorite)
        assertFalse(result[1].isFavorite)
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

        val breeds = listOf(breed)

        val result = BreedsUseCases().toggleFavourite(
            breeds= breeds,
            id = breed.id
        )

        assertEquals(breeds, result)
    }

    @Test
    fun toggleFavourite_unknownBreed_doesNothing() = runTest {

        val breed = FakeBreedsUi.persian.copy(
            isFavorite = false
        )

        val breeds =  listOf(breed)

        val result = BreedsUseCases().toggleFavourite(
            breeds,
            "unknown-id"
        )

        assertEquals(breeds, result)

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
            breeds = listOf(breed),
            breed.id
        )

        assertFalse(result.first().isFavorite)
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

        val result = BreedsUseCases().searchBreeds(query = "pers")

        assertEquals(
            1,
            result?.size
        )

        assertEquals(
            FakeBreedsModel.persian.id,
            result?.first()?.id
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

        val result = BreedsUseCases().searchBreeds(query = "xxxx")

        assertEquals(0,result?.size)
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

        val result = BreedsUseCases().searchBreeds(query = "pers")

        assertNull(result)
    }


}