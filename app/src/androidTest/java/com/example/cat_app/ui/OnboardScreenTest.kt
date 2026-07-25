package com.example.cat_app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.features.onboard.OnboardEvent
import com.example.cat_app.ui.features.onboard.ScreenOnboard
import com.example.cat_app.ui.navigation.Destination
import junit.framework.TestCase.assertEquals
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class OnboardScreenTest {

    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun onboardingScreen_menuVisible() {
        composeRule.setContent {
            ScreenOnboard(
                onEvent = {}
            )
        }

        composeRule
            .onNodeWithTag(TestTags.BREEDS_NAV_BUTTON)
            .assertExists()

        composeRule
            .onNodeWithTag(TestTags.FAVORITES_NAV_BUTTON)
            .assertExists()
    }

    @Test
    fun clickAllCats_emitsNavigateToBreeds() {

        var receivedEvent: OnboardEvent? = null

        composeRule.setContent {
            ScreenOnboard(
                onEvent = { receivedEvent = it }
            )
        }

        composeRule
            .onNodeWithTag(TestTags.BREEDS_NAV_BUTTON)
            .performClick()

        assertEquals(
            OnboardEvent.NavigateToBreeds,
            receivedEvent
        )
    }

}