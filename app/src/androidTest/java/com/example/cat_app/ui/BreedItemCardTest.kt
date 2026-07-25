package com.example.cat_app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cat_app.fake.BreedFakes
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.features.breeds.components.BreedItemCard
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class BreedItemCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun breedName_isDisplayed() {
        composeTestRule.setContent {
            BreedItemCard(
                breed = BreedFakes.persian,
                onCardClick = {},
                onFavoriteClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Persian")
            .assertExists()
        composeTestRule
            .onNodeWithText("Origin: Iran")
            .assertExists()
        composeTestRule
            .onNodeWithText("Affectionate")
            .assertExists()
    }

    @Test
    fun clickingCard_callsCallback() {
        var clicked = false
        composeTestRule.setContent {
            BreedItemCard(
                breed = BreedFakes.persian,
                onCardClick = {
                    clicked = true
                },
                onFavoriteClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Persian")
            .performClick()
        assertTrue(clicked)
    }

    @Test
    fun clickingFavorite_callsFavoriteCallback() {

        var favoriteClicked = false
        composeTestRule.setContent {
            BreedItemCard(
                breed = BreedFakes.persian,
                onCardClick = {},
                onFavoriteClick = {
                    favoriteClicked = true
                }
            )
        }

        composeTestRule
            .onNodeWithTag("favoriteButton")
            .performClick()

        assertTrue(favoriteClicked)
    }

    @Test
    fun favoriteFalse_showsBorderIcon() {

        composeTestRule.setContent {
            BreedItemCard(
                breed = BreedFakes.breedWithFavoriteStatus(false),
                onCardClick = {},
                onFavoriteClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAVORITE_ICON_UNSELECTED, useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun favoriteTrue_showsFilledIcon() {

        composeTestRule.setContent {
            BreedItemCard(
                breed = BreedFakes.breedWithFavoriteStatus(true),
                onCardClick = {},
                onFavoriteClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAVORITE_ICON_SELECTED, useUnmergedTree = true)
            .assertExists()
    }

}