package ru.adigital.composetutorial.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private val MaxChartValue = HorizontalAlignmentLine(merger = { old, new ->
    min(old, new)
})

private val MinChartValue = HorizontalAlignmentLine(merger = { old, new ->
    max(old, new)
})

@Composable
fun BarChart(
    dataPoints: List<Int>,
    modifier: Modifier = Modifier,
) {
    val maxValue: Float = remember(dataPoints) { dataPoints.maxOrNull()!! * 1.2f }

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        with(density) {
            // [START_EXCLUDE]
            val yPositionRatio = remember(density, maxHeight, maxValue) {
                maxHeight.toPx() / maxValue
            }
            val xPositionRatio = remember(density, maxWidth, dataPoints) {
                maxWidth.toPx() / (dataPoints.size + 1)
            }
            val xOffset = remember(density) { // center points in the graph
                xPositionRatio / dataPoints.size
            }
            // [END_EXCLUDE]
            // Calculate baselines
            val maxYBaseline = // [START_EXCLUDE]
                remember(dataPoints) {
                    dataPoints.maxOrNull()?.let {
                        (maxValue - it) * yPositionRatio
                    } ?: 0f
                } // [END_EXCLUDE]
            val minYBaseline = // [START_EXCLUDE]
                remember(dataPoints) {
                    dataPoints.minOrNull()?.let {
                        (maxValue - it) * yPositionRatio
                    } ?: 0f
                }
            // [END_EXCLUDE]
            Layout(
                content = {},
                modifier = Modifier.drawBehind {
                    // [START_EXCLUDE]
                    dataPoints.forEachIndexed { index, dataPoint ->
                        val rectSize = Size(60f, dataPoint * yPositionRatio)
                        val topLeftOffset = Offset(
                            x = xPositionRatio * (index + 1) - xOffset,
                            y = (maxValue - dataPoint) * yPositionRatio
                        )
                        drawRect(Color(0xFF3DDC84), topLeftOffset, rectSize)
                    }
                    drawLine(
                        Color(0xFF073042),
                        start = Offset(0f, 0f),
                        end = Offset(0f, maxHeight.toPx()),
                        strokeWidth = 6f
                    )
                    drawLine(
                        Color(0xFF073042),
                        start = Offset(0f, maxHeight.toPx()),
                        end = Offset(maxWidth.toPx(), maxHeight.toPx()),
                        strokeWidth = 6f
                    )
                    // [END_EXCLUDE]
                }
            ) { _, constraints ->
                with(constraints) {
                    layout(
                        width = if (hasBoundedWidth) maxWidth else minWidth,
                        height = if (hasBoundedHeight) maxHeight else minHeight,
                        // Custom AlignmentLines are set here. These are propagated
                        // to direct and indirect parent composables.
                        alignmentLines = mapOf(
                            MinChartValue to minYBaseline.roundToInt(),
                            MaxChartValue to maxYBaseline.roundToInt()
                        )
                    ) {}
                }
            }
        }
    }
}

@Composable
private fun BarChartMinMax(
    dataPoints: List<Int>,
    maxText: @Composable () -> Unit,
    minText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        content = {
            maxText()
            minText()
            // Set a fixed size to make the example easier to follow
            BarChart(dataPoints, Modifier.size(200.dp))
        },
        modifier = modifier
    ) { measurables, constraints ->
        check(measurables.size == 3)
        val placeables = measurables.map {
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val maxTextPlaceable = placeables[0]
        val minTextPlaceable = placeables[1]
        val barChartPlaceable = placeables[2]

        // Obtain the alignment lines from BarChart to position the Text
        val minValueBaseline = barChartPlaceable[MinChartValue]
        val maxValueBaseline = barChartPlaceable[MaxChartValue]
        layout(constraints.maxWidth, constraints.maxHeight) {
            maxTextPlaceable.placeRelative(
                x = 0,
                y = maxValueBaseline - (maxTextPlaceable.height / 2)
            )
            minTextPlaceable.placeRelative(
                x = 0,
                y = minValueBaseline - (minTextPlaceable.height / 2)
            )
            barChartPlaceable.placeRelative(
                x = max(maxTextPlaceable.width, minTextPlaceable.width) + 20,
                y = 0
            )
        }
    }
}

@Preview
@Composable
private fun ChartDataPreview() {
    MaterialTheme {
        BarChart(listOf(1, 2, 4, 10, 5, 3))

        BarChartMinMax(
            dataPoints = listOf(4, 24, 15),
            maxText = { Text("Max") },
            minText = { Text("Min") },
            modifier = Modifier.padding(24.dp)
        )
    }
}
