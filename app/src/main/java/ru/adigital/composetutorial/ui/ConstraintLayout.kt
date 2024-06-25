package ru.adigital.composetutorial.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import ru.adigital.composetutorial.ui.theme.ComposeTutorialTheme

// 1
@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        val (button, text) = createRefs()

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }

        Text(
            "Text",
            Modifier.constrainAs(text) {
                top.linkTo(button.bottom, margin = 16.dp)
            }
        )
    }
}

// 2
@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (minWidth < 600.dp) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

// 3 Guideline
@Composable
fun Guideline() {
    ConstraintLayout {
        val (text) = createRefs()

        val topGuideline = createGuidelineFromTop(0.25f)

        Text(
            "Text",
            Modifier.constrainAs(text) {
                top.linkTo(topGuideline)
                bottom.linkTo(topGuideline)
            }
        )
    }
}

// 4 Barrier
@Composable
private fun ConstraintLayoutBarrier() {
    ConstraintLayout {
        val (button, text, button2) = createRefs()

        val endBarrier = createEndBarrier(button, text)

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top)
            }
        ) {
            Text("Button")
        }

        Text(
            "Text qwe qw eqwe",
            Modifier.constrainAs(text) {
                top.linkTo(button.bottom)
            }
        )

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(text.bottom)
                start.linkTo(endBarrier)
            }
        ) {
            Text("Button2")
        }

    }
}

// 5 Chain
@Composable
private fun ConstraintLayoutChain() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (button, text) = createRefs()

        createVerticalChain(button, text, chainStyle = ChainStyle.Spread)

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top)
            }
        ) {
            Text("Button")
        }

        Text(
            "Text qwe qw eqwe",
            Modifier.constrainAs(text) {
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewConstraintLayoutContent() {
    ComposeTutorialTheme {
        Column {
//            ConstraintLayoutContent()
//            DecoupledConstraintLayout()
//            Guideline()
//            ConstraintLayoutBarrier()
            ConstraintLayoutChain()
        }
    }
}