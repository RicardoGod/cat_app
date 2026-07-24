package com.example.cat_app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cat_app.fake.BreedFakes
import com.example.cat_app.ui.features.breeds.components.BreedList
import com.example.cat_app.ui.features.breeds.model.BreedUi
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class BreedListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun list_displaysAllBreeds() {

        composeTestRule.setContent {

            BreedList(

                breeds = listOf(
                    BreedFakes.persian,
                    BreedFakes.siamese,
                    BreedFakes.bengal
                ),
                onBreedClick = {},
                onFavoriteClick = {}
            )
        }

        composeTestRule.onNodeWithText("Persian").assertExists()

        composeTestRule.onNodeWithText("Siamese").assertExists()

        composeTestRule.onNodeWithText("Bengal").assertExists()
    }

    @Test
    fun clickingBreed_callsCallback() {

        var selectedBreed: BreedUi? = null

        composeTestRule.setContent {

            BreedList(

                breeds = listOf(BreedFakes.persian),

                onBreedClick = {
                    selectedBreed = it
                },
                onFavoriteClick = {}
            )

        }

        composeTestRule
            .onNodeWithText("Persian")
            .performClick()

        assertEquals("Persian", selectedBreed?.name)
    }
}