package com.example.cat_app.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.cat_app.fake.BreedFakes
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.features.breeds.BreedsEvent
import com.example.cat_app.ui.features.breeds.ScreenBreeds
import com.example.cat_app.ui.features.breeds.components.BreedList
import com.example.cat_app.ui.features.breeds.model.BreedsUiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class BreedsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingIndicator_isDisplayed() {

        composeTestRule.setContent {

            ScreenBreeds(
                state = BreedsUiState(
                    isLoading = true
                ),

                onEvent = {},
                navigateBack = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.LOADING)
            .assertExists()
    }

    @Test
    fun noBreeds_showEmptyMessage() {

        composeTestRule.setContent {

            ScreenBreeds(
                state = BreedsUiState(
                    breeds = emptyList()
                ),
                onEvent = {},
                navigateBack = {}
            )
        }

        composeTestRule.onNodeWithText("No breeds found", true).assertExists()
    }

    @Test
    fun withResults_showBreedsList() {

        composeTestRule.setContent {

            ScreenBreeds(
                state = BreedsUiState(
                    breeds = listOf(
                        BreedFakes.persian,
                        BreedFakes.bengal,
                        BreedFakes.siamese
                    )
                ),
                onEvent = {},
                navigateBack = {}
            )
        }

        composeTestRule.onNodeWithText("Persian").assertExists()

        composeTestRule.onNodeWithText("Siamese").assertExists()

        composeTestRule.onNodeWithText("Bengal").assertExists()
    }

    @Test
    fun search_triggerEvent(){
        var receivedEvent: BreedsEvent? = null

        composeTestRule.setContent {

            ScreenBreeds(

                state = BreedsUiState(),

                onEvent = {
                    receivedEvent = it
                },
                navigateBack = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD)
            .performTextInput("Persian")

        assertEquals(
            BreedsEvent.SearchChanged("Persian"),
            receivedEvent
        )
    }

    @Test
    fun clickingFavorite_emitsToggleFavoriteEvent() {

        var received: BreedsEvent? = null

        composeTestRule.setContent {

            ScreenBreeds(
                state = BreedsUiState(
                    breeds = listOf(BreedFakes.persian),
                    selectedBreed = BreedFakes.persian
                ),
                onEvent = {
                    received = it
                },
                navigateBack = {}
            )

        }

        composeTestRule
            .onNodeWithTag(TestTags.FAVORITE_BUTTON)
            .performClick()

        assertTrue(received is BreedsEvent.ToggleFavorite)
    }

    @Test
    fun clickingFavorite_changesIcon() {
        var state by mutableStateOf(
            BreedsUiState(
                breeds = listOf(BreedFakes.breedWithFavoriteStatus(false)),
            )
        )

        composeTestRule.setContent {
            ScreenBreeds(
                state = state,
                onEvent = {},
                navigateBack = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAVORITE_ICON_UNSELECTED, useUnmergedTree = true)
            .assertExists()


        composeTestRule.runOnUiThread {
            state = BreedsUiState(
                breeds = listOf(BreedFakes.breedWithFavoriteStatus(true)),
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAVORITE_ICON_SELECTED, useUnmergedTree = true)
            .assertExists()

    }

}