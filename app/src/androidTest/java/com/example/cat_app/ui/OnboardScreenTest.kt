package com.example.cat_app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.features.onboard.ScreenOnboard
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

}