package com.example.cat_app.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cat_app.fake.BreedFakes
import com.example.cat_app.ui.features.breeds.components.BreedDialog
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class BreedDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun dialog_displaysBreedInformation() {

        composeTestRule.setContent {

            BreedDialog(
                breed = BreedFakes.persian,
                onDismiss = {}
            )

        }

        composeTestRule.onNodeWithText("Persian", true).assertExists()

        composeTestRule.onNodeWithText("Iran", true).assertExists()

        composeTestRule.onNodeWithText("Affectionate", true).assertExists()

        composeTestRule.onNodeWithText("Close").assertExists()
    }

    @Test
    fun clickingClose_callsDismiss() {

        var dismissed = false

        composeTestRule.setContent {

            BreedDialog(
                breed = BreedFakes.persian,
                onDismiss = {
                    dismissed = true
                }
            )

        }

        composeTestRule
            .onNodeWithText("Close")
            .performClick()

        assertTrue(dismissed)
    }

}