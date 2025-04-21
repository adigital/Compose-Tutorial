package ru.adigital.composetutorial

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.adigital.composetutorial.ui.Conversation
import ru.adigital.composetutorial.ui.SharedElementPredictiveBack
import ru.adigital.composetutorial.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
//                    MessageCard(Message("Android", "Jetpack Compose"))
//                    Conversation(SampleData.conversationSample)
//                    DialogExamples()
//                    AnimatedContentWithoutAnySharedElementTransitions()
//                    SharedElementTransitionBetweenTwoComposables()
//                    SharedElementManualVisibleControl()
//                    UnmatchedBoundsExample()
                    SharedElementPredictiveBack()
                }
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun Preview() {
    ComposeTutorialTheme {
        Conversation(SampleData.conversationSample)
    }
}