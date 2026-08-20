package com.example.cat_app.ui.features.breeds

import com.example.cat_app.helper.FakeBreedsUi
import com.example.cat_app.helper.MainDispatcherRule
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class BreedsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    val useCase: BreedsUseCases = mock()

    @Test
    fun loadBreeds_success_updatesState() = runTest {
        val fakeBreed = FakeBreedsUi.persian
        val state = fakeBreedsUiState(fakeBreed)

        // Given
        whenever(useCase.fetchBreeds(BreedsUiState())).thenReturn(state)

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
        val state = fakeBreedsUiState(breed)

        whenever(useCase.fetchBreeds(any())).thenReturn(state)

        val vm = BreedsViewModel(useCase)

        vm.onEvent(BreedsEvent.LoadingScreen)

        advanceUntilIdle()

        assertEquals(1, vm.state.value.breeds.size)
        assertEquals(breed, vm.state.value.breeds.first())
    }

    @Test
    fun breedClicked_updatesSelectedBreed() {

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.BreedClicked(FakeBreedsUi.persian)
        )

        assertEquals(
            FakeBreedsUi.persian,
            vm.state.value.selectedBreed
        )
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
            useCase.searchBreeds(any(), eq("pers"))
        ).thenReturn(
            fakeBreedsUiState(breed)
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
    fun toggleFavourite_updatesBreed() = runTest {

        val favourite = FakeBreedsUi.persian.copy(
            isFavorite = true
        )

        whenever(
            useCase.toggleFavourite(any(), eq(favourite.id))
        ).thenReturn(
            fakeBreedsUiState(favourite)
        )

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.ToggleFavorite(favourite)
        )

        advanceUntilIdle()

        assertEquals(
            true,
            vm.state.value.breeds.first().isFavorite
        )
    }

    @Test
    fun loadingScreen_callsUseCase() = runTest {

        whenever(useCase.fetchBreeds(any()))
            .thenReturn(BreedsUiState())

        val vm = BreedsViewModel(useCase)

        vm.onEvent(BreedsEvent.LoadingScreen)

        advanceUntilIdle()

        verify(useCase).fetchBreeds(any())
    }

    @Test
    fun searchChanged_passesCorrectQuery() = runTest {

        whenever(
            useCase.searchBreeds(any(), any())
        ).thenReturn(BreedsUiState())

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.SearchChanged("siam")
        )

        advanceUntilIdle()

        verify(useCase).searchBreeds(any(), eq("siam"))
    }

    @Test
    fun toggleFavourite_callsUseCase() = runTest {

        whenever(
            useCase.toggleFavourite(any(), any())
        ).thenReturn(BreedsUiState())

        val vm = BreedsViewModel(useCase)

        vm.onEvent(
            BreedsEvent.ToggleFavorite(FakeBreedsUi.persian)
        )

        advanceUntilIdle()

        verify(useCase)
            .toggleFavourite(any(), eq(FakeBreedsUi.persian.id))
    }



    fun fakeBreedsUiState(breedUi: BreedUi) = BreedsUiState(
            breeds = listOf(breedUi),
            selectedBreed = breedUi
    )
}