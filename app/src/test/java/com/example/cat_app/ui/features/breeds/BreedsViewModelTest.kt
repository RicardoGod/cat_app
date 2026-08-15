package com.example.cat_app.ui.features.breeds

import com.example.cat_app.helper.FakeBreedsUi
import com.example.cat_app.helper.MainDispatcherRule
import com.example.cat_app.ui.features.breeds.model.BreedUi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class BreedsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    val useCase: BreedsUseCases = mock()

    @Test
    fun loadingScreen_callsUseCase() = runTest {

        whenever(useCase.fetchBreeds(any(), any()))
            .thenReturn(listOf())

        val vm = BreedsViewModel(useCase)

        vm.onEvent(BreedsEvent.LoadingScreen)

        advanceUntilIdle()

        verify(useCase).fetchBreeds(any(), any())
    }

    @Test
    fun loadBreeds_success_updatesState() = runTest {
        val fakeBreed = FakeBreedsUi.persian

        // Given
        whenever(useCase.fetchBreeds(any(), any()))
            .thenReturn(listOf(fakeBreed))

        // When
        val vm = BreedsViewModel(useCase)

        vm.onEvent(BreedsEvent.LoadingScreen)

        advanceUntilIdle()

        // Then
        assertEquals(listOf(fakeBreed), vm.state.value.breeds)
    }

    @Test
    fun initialState_isEmpty() {
        val vm = BreedsViewModel(useCase)

        assertEquals(emptyList<BreedUi>(), vm.state.value.breeds)
        assertEquals(null, vm.state.value.selectedBreed)
        assertEquals(null, vm.state.value.error)
    }

    @Test
    fun loadingScreen_loadsBreeds() = runTest {

        val breed = FakeBreedsUi.persian

        whenever(useCase.fetchBreeds(any(), any()))
            .thenReturn(listOf(breed))

        val vm = BreedsViewModel(useCase)

        vm.onEvent(BreedsEvent.LoadingScreen)

        advanceUntilIdle()

        assertEquals(1, vm.state.value.breeds.size)
        assertEquals(breed, vm.state.value.breeds.first())
    }

    @Test
    fun closeDialog_unselectsBreed() {

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.BreedClicked(FakeBreedsUi.persian)
        )

        vm.onEvent(BreedsEvent.CloseDialog)

        assertEquals(
            null,
            vm.state.value.selectedBreed
        )
    }

    @Test
    fun searchChanged_updatesState() = runTest {

        val breed = FakeBreedsUi.persian

        whenever(
            useCase.searchBreeds("pers")
        ).thenReturn(
            listOf(breed)
        )

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.SearchChanged("pers")
        )

        advanceUntilIdle()

        assertEquals(
            listOf(breed),
            vm.state.value.breeds
        )
    }

    @Test
    fun toggleFavourite_callsUseCase() = runTest {

        whenever(
            useCase.toggleFavourite(any(), any())
        ).thenReturn(listOf())

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.ToggleFavorite(FakeBreedsUi.persian)
        )

        advanceUntilIdle()

        verify(useCase)
            .toggleFavourite(any(), eq(FakeBreedsUi.persian.id))
    }

    @Test
    fun toggleFavourite_updatesBreed() = runTest {

        val favourite = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        whenever(
            useCase.toggleFavourite(any(), eq(favourite.id))
        ).thenReturn(
            listOf(favourite)
        )


        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.ToggleFavorite(favourite)
        )

        advanceUntilIdle()

        verify(useCase).toggleFavourite(
            any(),
            eq(favourite.id)
        )

        assertEquals(
            true,
            vm.state.value.breeds.find { it.id == favourite.id } ?.isFavorite
        )
    }


    @Test
    fun searchChanged_passesCorrectQuery() = runTest {

        whenever(
            useCase.searchBreeds(any())
        ).thenReturn(listOf())

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.SearchChanged("siam")
        )

        advanceUntilIdle()

        verify(useCase).searchBreeds("siam")
    }

    @Test
    fun searchChanged_updatesSearchQuery() = runTest {

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.SearchChanged("Aby")
        )

        assertEquals(
            "Aby",
            vm.searchQuery.value
        )
    }

    @Test
    fun searchChanged_searchesAfterDebounce() = runTest {

        val vm = BreedsViewModel(useCase)

        val expectedState = listOf(
            FakeBreedsUi.persian
        )

        whenever(
            useCase.searchBreeds("Persian")
        ).thenReturn(expectedState)

        vm.onEvent(
            BreedsEvent.SearchChanged("Persian")
        )

        // Here it should not be called
        verify(useCase, never()).searchBreeds("Persian")

        advanceTimeBy(300.milliseconds)

        advanceUntilIdle()

        verify(useCase).searchBreeds("Persian")

        assertEquals(
            expectedState,
            vm.state.value.breeds
        )
    }

    @Test
    fun searchChangedMultipleTimes_debounceRapidInput() = runTest {

        val vm = BreedsViewModel(useCase)

        whenever(
            useCase.searchBreeds("Aby")
        ).thenReturn(
            listOf(FakeBreedsUi.persian)
        )

        vm.onEvent(BreedsEvent.SearchChanged("A"))
        vm.onEvent(BreedsEvent.SearchChanged("Ab"))
        vm.onEvent(BreedsEvent.SearchChanged("Aby"))

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        verify(useCase, never()).searchBreeds("A")

        verify(useCase, never()).searchBreeds("Ab")

        verify(useCase).searchBreeds("Aby")
    }

    @Test
    fun clearSearch_fetchesBreedsAgain() = runTest {

        val vm = BreedsViewModel(useCase)

        val expectedSearch = listOf(
            FakeBreedsUi.persian
        )

        whenever(
            useCase.searchBreeds("Aby")
        ).thenReturn(expectedSearch)

        whenever(
            useCase.fetchBreeds(any(), any())
        ).thenReturn(
            expectedSearch
        )

        vm.onEvent(
            BreedsEvent.SearchChanged("Aby")
        )

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        assertEquals(
            "Aby",
            vm.searchQuery.value
        )

        vm.onEvent(
            BreedsEvent.ClearSearch
        )

        assertEquals(
            "",
            vm.searchQuery.value
        )

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        verify(useCase).fetchBreeds(any(), any())
    }

    @Test
    fun searchChanged_toEmpty_fetchesBreeds() = runTest {

        val vm = BreedsViewModel(useCase)

        whenever(
            useCase.fetchBreeds(any(), any())
        ).thenReturn(
            listOf(FakeBreedsUi.persian)
        )

        vm.onEvent(
            BreedsEvent.SearchChanged("Aby")
        )

        advanceTimeBy(300.milliseconds)

        vm.onEvent(
            BreedsEvent.SearchChanged("")
        )

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        verify(useCase).fetchBreeds(any(), any())
    }

    @Test
    fun searchChanged_sameQuery_doesNotSearchTwice() = runTest {

        val vm = BreedsViewModel(useCase)

        whenever(
            useCase.searchBreeds("Aby")
        ).thenReturn(
            listOf(FakeBreedsUi.persian)
        )

        vm.onEvent(BreedsEvent.SearchChanged("Aby"))

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        vm.onEvent(BreedsEvent.SearchChanged("Aby"))

        advanceTimeBy(300.milliseconds)
        advanceUntilIdle()

        verify(
            useCase,
            times(1)
        ).searchBreeds("Aby")

    }

    @Test
    fun breedClicked_updatesSelectedBreed() = runTest {

        val breed = FakeBreedsUi.persian

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.BreedClicked(breed)
        )

        assertEquals(
            breed,
            vm.state.value.selectedBreed
        )
    }

    @Test
    fun closeDialog_clearsSelectedBreed() = runTest {

        val breed = FakeBreedsUi.persian

        val vm = BreedsViewModel(useCase)

        vm.onEvent(BreedsEvent.BreedClicked(breed))
        vm.onEvent(BreedsEvent.CloseDialog)

        assertEquals(
            null,
            vm.state.value.selectedBreed
        )
    }
}