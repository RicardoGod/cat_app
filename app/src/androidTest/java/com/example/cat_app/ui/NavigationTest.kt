package com.example.cat_app.ui

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cat_app.ui.common.TestTags
import com.example.cat_app.ui.navigation.AppNavigation
import com.example.cat_app.ui.navigation.Destination
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    lateinit var navController: TestNavHostController

    @Before
    fun setup() {

        composeRule.setContent {

            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            AppNavigation(
                navController = navController,
                startDestination = Destination.Onboard.route
            )
        }
    }

    @Test
    fun startsOnOnboarding() {
        assertEquals(
            Destination.Onboard.route,
            navController.currentDestination?.route
        )
    }

    @Test
    fun clickBreeds_navigateToBreeds() {

        composeRule.onRoot().printToLog("TREE")

        composeRule
            .onNodeWithTag(TestTags.BREEDS_NAV_BUTTON)
            .performClick()

        assertEquals(
            Destination.Breeds.route,
            navController.currentDestination?.route

        )
    }

    @Test
    @Ignore("Favorites not implemented yet")
    fun clickFavorites_navigateToBreeds() {
        composeRule
            .onNodeWithTag(TestTags.FAVORITES_NAV_BUTTON)
            .performClick()

        assertEquals(
            Destination.Favorites.route,
            navController.currentDestination?.route

        )
    }
}