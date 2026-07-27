package com.example.cat_app.ui.features.breeds

import com.example.cat_app.data.services.BreedsService
import com.example.cat_app.helper.FakeBreeds
import com.example.cat_app.helper.MainDispatcherRule
import com.example.cat_app.ui.features.breeds.model.BreedUi
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.get
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever


class BreedsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    val useCase: BreedsUseCases = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadBreeds_success_updatesState() = runTest {
        val fakeBreed = FakeBreeds.persian
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


    fun fakeBreedsUiState(breedUi: BreedUi) = BreedsUiState(
            breeds = listOf(breedUi),
            selectedBreed = breedUi
    )
}