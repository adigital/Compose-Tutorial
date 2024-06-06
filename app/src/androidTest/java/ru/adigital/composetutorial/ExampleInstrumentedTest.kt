package ru.adigital.composetutorial

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.adigital.composetutorial", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class HomeTest {
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun home_navigatesToAllScreens() {
        composeTestRule.onNodeWithText("Jetpack Compose").assertIsDisplayed()
//        composeTestRule.onNodeWithText("SLEEP").performClick()
//        composeTestRule.onNodeWithText("Explore Properties by Destination").assertIsDisplayed()
//        composeTestRule.onNodeWithText("EAT").performClick()
//        composeTestRule.onNodeWithText("Explore Restaurants by Destination").assertIsDisplayed()
//        composeTestRule.onNodeWithText("FLY").performClick()
//        composeTestRule.onNodeWithText("Explore Flights by Destination").assertIsDisplayed()
    }
}