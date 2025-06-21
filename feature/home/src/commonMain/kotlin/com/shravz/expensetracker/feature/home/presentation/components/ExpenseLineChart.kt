package com.shravz.expensetracker.feature.home.presentation.components

import androidx.collection.size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shravz.expensetracker.feature.home.model.Expense
import com.shravz.expensetracker.feature.home.presentation.model.ExpenseTimeRange
import kotlin.math.roundToInt

private fun formatDateForXAxis(dateString: String): String {
    return try {
        dateString.substring(5) // "YYYY-MM-DD" -> "MM-DD"
    } catch (e: Exception) {
        dateString // Fallback
    }
}

@Composable
fun ExpenseLineChart(
    expenses: List<Expense>,
    selectedTimeRange: ExpenseTimeRange,
    onTimeRangeSelected: (ExpenseTimeRange) -> Unit,
    isLoading: Boolean,
    chartTitle: String,
    modifier: Modifier = Modifier, // Modifier for the Card itself
    onDataPointClick: (Expense) -> Unit = {}
) {
    val sortedExpenses = expenses.sortedBy { it.date }
    val maxAmount = if (sortedExpenses.isNotEmpty()) sortedExpenses.maxOf { it.amount } else 0.0
    val minAmount = 0.0

    val lineColor = Color.White
    val selectedDataPointColor = Color(0xFFE56717)
    val fillColor = Color.White.copy(alpha = 0.2f)
    val gridLineColor = Color.White.copy(alpha = 0.3f)
    val axisLabelColor = Color.White.copy(alpha = 0.7f)

    var tappedExpense by remember { mutableStateOf<Expense?>(null) }
    var gastosGraphCoordinates by remember { mutableStateOf<List<Offset>>(emptyList()) }
    val density = LocalDensity.current

    val chartPaddingTopDp = 16.dp
    val chartPaddingEndDp = 10.dp
    val chartActualBottomPaddingDp = 10.dp
    val yAxisLabelWidth = 35.dp
    val xAxisLabelAreaHeight = 24.dp

    Card(
        modifier = modifier // Apply the passed modifier to the Card
            .fillMaxWidth()
            .height(250.dp), // You might want to make this dynamic or pass as a parameter
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Column for Title and Chart on the right

            // 1. Remember a TextMeasurer
            val textMeasurer = rememberTextMeasurer()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (sortedExpenses.isNotEmpty()) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Chart area takes remaining vertical space
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) { // Row for Y-Labels and Canvas
                            // Y-Axis Labels Column
                            val numYLabels = 4
                            Column(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .width(yAxisLabelWidth)
                                    .padding(
                                        top = 5.dp,
                                        bottom = chartActualBottomPaddingDp,
                                        end = 5.dp
                                    ),
                                horizontalAlignment = Alignment.End
                            ) {
                                (0..numYLabels).reversed().forEach { i ->
                                    val amount =
                                        minAmount + (maxAmount - minAmount) * i / numYLabels
                                    Text(
                                        text = amount.roundToInt().toString(),
                                        color = axisLabelColor,
                                        fontSize = 10.sp,
                                        modifier = Modifier.height(40.dp)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(
                                        top = chartPaddingTopDp,
                                        end = chartPaddingEndDp,
                                        bottom = chartActualBottomPaddingDp
                                    ),
                                verticalArrangement = Arrangement.Top
                            ) {
                                // Canvas for the Chart
                                Canvas(
                                    modifier = Modifier.weight(1f)
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                        .pointerInput(sortedExpenses) {
                                            detectTapGestures { tapOffset ->
                                                val clickedIndex =
                                                    gastosGraphCoordinates.indexOfFirst { point ->
                                                        (tapOffset - point).getDistanceSquared() < with(
                                                            density
                                                        ) { 12.dp.toPx() }.let { it * it }
                                                    }
                                                if (clickedIndex != -1) {
                                                    val expense = sortedExpenses[clickedIndex]
                                                    tappedExpense = expense
                                                    onDataPointClick(expense)
                                                } else {
                                                    tappedExpense = null
                                                }
                                            }
                                        }
                                ) {
                                    val chartDrawableWidth = size.width
                                    val chartDrawableHeight = size.height

                                    if (chartDrawableHeight <= 0f || chartDrawableWidth <= 0f) return@Canvas

                                    (0..numYLabels).forEach { i ->
                                        val y = chartDrawableHeight * (numYLabels - i) / numYLabels
                                        drawLine(
                                            color = gridLineColor,
                                            start = Offset(0f, y),
                                            end = Offset(chartDrawableWidth, y),
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }

                                    val dataPoints = sortedExpenses.mapIndexed { index, expense ->
                                        val x =
                                            if (sortedExpenses.size == 1) chartDrawableWidth / 2f
                                            else chartDrawableWidth * index / (sortedExpenses.size - 1).coerceAtLeast(
                                                1
                                            )

                                        val yRange = (maxAmount - minAmount).coerceAtLeast(1.0)
                                        val yValue =
                                            ((expense.amount - minAmount) / yRange).toFloat()
                                        val y = chartDrawableHeight * (1 - yValue)
                                        DataPoint(expense.amount, Offset(x.toFloat(), y.coerceIn(0f, chartDrawableHeight)))
                                    }
                                    val points = dataPoints.map { it.center }
                                    gastosGraphCoordinates = points

                                    if (points.isNotEmpty()) {
                                        val linePath = Path().apply {
                                            moveTo(
                                                points.first().x,
                                                points.first().y
                                            )
                                        }
                                        val fillPath = Path().apply {
                                            moveTo(points.first().x, chartDrawableHeight)
                                            lineTo(points.first().x, points.first().y)
                                        }

                                        if (points.size > 1) {
                                            for (i in 0 until points.size - 1) {
                                                val p1 = points[i];
                                                val p2 = points[i + 1]
                                                val cX1 = p1.x + (p2.x - p1.x) * 0.5f;
                                                val cY1 = p1.y
                                                val cX2 = cX1;
                                                val cY2 = p2.y
                                                linePath.cubicTo(cX1, cY1, cX2, cY2, p2.x, p2.y)
                                                fillPath.cubicTo(cX1, cY1, cX2, cY2, p2.x, p2.y)
                                            }
                                        } else {
                                            linePath.lineTo(points.first().x, points.first().y)
                                            fillPath.lineTo(points.first().x, points.first().y)
                                        }

                                        drawPath(
                                            path = linePath, color = lineColor,
                                            style = Stroke(
                                                width = 2.dp.toPx(),
                                                pathEffect = PathEffect.cornerPathEffect(16.dp.toPx())
                                            )
                                        )
                                        fillPath.lineTo(
                                            points.last().x,
                                            chartDrawableHeight
                                        ); fillPath.close()
                                        drawPath(path = fillPath, color = fillColor)

                                        val pointRadiusPx = with(density) { 4.dp.toPx() }
                                        dataPoints.forEachIndexed { index, dataPoint ->
                                            val point = dataPoint.center
                                            val isTapped =
                                                sortedExpenses.getOrNull(index) == tappedExpense
                                            drawCircle(
                                                color = if (isTapped) selectedDataPointColor else lineColor,
                                                radius = if (isTapped) pointRadiusPx * 1.5f else pointRadiusPx,
                                                center = point,
                                            )
                                            if(isTapped){
                                                drawCircle(
                                                    color = lineColor,
                                                    radius = pointRadiusPx * 0.8f,
                                                    center = point,
                                                )
                                                val rectWidth = 120 * 0.8f
                                                val rectHeight = 70 * 0.7f
                                                val topLeftX = point.x + (120 - rectWidth) / 2
                                                val topLeftY = point.y - (rectHeight* 1.5f ) + (70 - rectHeight) / 2
                                                val cornerRadius = 3.dp.toPx()

                                                drawRoundRect(
                                                    color = selectedDataPointColor,
                                                    topLeft = Offset(topLeftX, topLeftY),
                                                    size = Size(rectWidth, rectHeight),
                                                    cornerRadius = CornerRadius(cornerRadius,cornerRadius)
                                                )

                                                // 2. Measure the text
                                                val textLayoutResult = textMeasurer.measure(
                                                    text = AnnotatedString(dataPoint.amount.toString()),
                                                    style = TextStyle(
                                                        color = Color.White,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )

                                                // 3. Calculate text position for centering
                                                val textTopLeftX = topLeftX + (rectWidth - textLayoutResult.size.width) / 2
                                                val textTopLeftY = topLeftY + (rectHeight - textLayoutResult.size.height) / 2

                                                // 4. Draw the text (platform-agnostic)
                                                drawText(
                                                    textLayoutResult = textLayoutResult,
                                                    topLeft = Offset(textTopLeftX, textTopLeftY)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Column for Vertical Time Range Buttons on the left
                                Row(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    ExpenseTimeRange.entries.forEach { range ->
                                        val isSelected = selectedTimeRange == range
                                        Text(
                                            range.displayName, maxLines = 1,
                                            modifier = Modifier.clickable {
                                                onTimeRangeSelected(range)
                                            }.background(
                                                color = if (isSelected) {
                                                    Color.White.copy(alpha = 0.3f)
                                                } else {
                                                    Color.Transparent
                                                },
                                                shape = RoundedCornerShape(4.dp)
                                            ).padding(vertical = 6.dp, horizontal = 15.dp),
                                            color = if (isSelected) {
                                                Color.White
                                            } else {
                                                Color.White.copy(alpha = 0.7f)
                                            },
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (!isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No expense data to display.", color = Color.White.copy(alpha = 0.7f))
                    }
                }
                // If isLoading is true and expenses are empty, a loading indicator might be shown here,
                // or handled by the caller (HomeScreen in this case).
            }
        }
    }

}


data class DataPoint(val amount: Double, val center: Offset)