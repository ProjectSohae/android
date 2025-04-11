package com.sohae.common.ui.custom.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.graphics.ColorUtils
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import kotlin.math.abs

@Composable
fun WheelPickerDialog(
    hazeState: HazeState,
    initIdx: Int = 0,
    suffix: String = "",
    intensity: Float,
    onDismissRequest: () -> Unit,
    onConfirm: (Any) -> Unit,
    optionList: List<String>
) {
    val optionListSize = optionList.size
    val scrollInteraction = remember { MutableInteractionSource() }
    var scrollTotalDelta by remember { mutableIntStateOf(0) }
    var scrollDelta by remember { mutableIntStateOf(0) }
    var scrollDir by remember { mutableIntStateOf(0) }
    var isUndo by remember { mutableStateOf(false) }
    val initialZIndex = listOf( 0, 100, 200, 300, 200, 100, 0 )
    val initialAlpha = listOf( 0.4f, 0.6f, 0.8f, 1f, 0.8f, 0.6f, 0.4f)
    val initialScaleXY = listOf( 0.5f, 0.6f, 0.8f, 1f, 0.8f, 0.6f, 0.5f )
    val initialRotateX = listOf( 150f, 75f, 30f, 0f, -30f, -75f, -150f )
    val initialOffsetY = listOf( -90, -142, -94, 0, 94, 142, 90 )
    val currentZIndex = mutableListOf( 0, 100, 200, 300, 200, 100, 0 )
    val currentScaleXY = mutableListOf( 0.5f, 0.6f, 0.8f, 1f, 0.8f, 0.6f, 0.5f )
    val currentRotateX = mutableListOf( 150f, 75f, 30f, 0f, -30f, -75f, -150f )
    val currentOffsetY = mutableListOf( -20, -72, -24, 70, 164, 212, 160)
    val currentAlpha = mutableListOf( 0.4f, 0.6f, 0.8f, 1f, 0.8f, 0.6f, 0.4f)
    // currentIdx(i): i-th item's position on UI is equal to currentIdx(i)-th's
    val currentIdx = mutableListOf( 0, 1, 2, 3, 4, 5, 6 )
    val reverseCurrentIdx = mutableListOf( 0, 1, 2, 3, 4, 5, 6 )
    val targetIdx = mutableListOf( 0, 1, 2, 3, 4, 5, 6 )
    var startOptionValueIdx: Int
    var lastOptionValueIdx: Int
    val currentOptionValueIdx = mutableListOf(
        ((optionListSize - 1) - (2 % optionListSize) + initIdx) % optionListSize,
        ((optionListSize - 1) - (1 % optionListSize) + initIdx) % optionListSize,
        ((optionListSize - 1) + initIdx) % optionListSize,
        initIdx,
        ((initIdx + 1) % optionListSize),
        ((initIdx + 2) % optionListSize),
        ((initIdx + 3) % optionListSize)
    )
    val calcCurrentFloatValue: (Float, Float, Int) -> Float = { current, target, delta ->
        current + ((target - current) * (delta % 100)) / 100f
    }
    val calcCurrentIntValue: (Int, Int, Int) -> Int = { current, target, delta ->
        current + ((target - current) * (delta % 100)) / 100
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    // positive delta <- scroll downward
                    // negative delta <- scroll upward
                    state = rememberScrollableState { delta ->
                        scrollDelta = delta.toInt()

                        if (scrollDelta < 0) {

                            if (scrollDir > 0) {
                                scrollTotalDelta = 100 - (scrollTotalDelta % 100)
                                isUndo = true
                            }

                            scrollDir = -1
                        } else if (scrollDelta > 0) {

                            if (scrollDir < 0) {
                                scrollTotalDelta = 100 - (scrollTotalDelta % 100)
                                isUndo = true
                            }

                            scrollDir = 1
                        } else {
                            isUndo = false
                        }

                        scrollTotalDelta += abs(scrollDelta)

                        delta
                    },
                    interactionSource = scrollInteraction
                ),
            contentAlignment = Alignment.Center
        ) {
            val isDragged = scrollInteraction.collectIsDraggedAsState().value

            // uppermost position on UI
            startOptionValueIdx = reverseCurrentIdx[0]
            // lowermost position on UI
            lastOptionValueIdx = reverseCurrentIdx[6]

            if (!isDragged) {
                if (scrollDelta == 0) {
                    if (scrollTotalDelta <= 50) { scrollTotalDelta /= 2 }
                    else if (scrollTotalDelta < 100) {
                        scrollDelta = ((100 - scrollTotalDelta) / 2) * (scrollDir)

                        if (scrollTotalDelta % 2 == 1) { scrollDelta += scrollDir }

                        scrollTotalDelta += abs(scrollDelta)
                    }
                }
            }

            for (idx: Int in 0..6) {

                if (isUndo) { currentIdx[idx] = targetIdx[idx] }

                if (abs(scrollDelta) > 0) {

                    // scroll upward
                    if (scrollDelta < 0) {

                        if (currentIdx[idx] - (scrollTotalDelta / 100) < 0) {
                            currentOptionValueIdx[idx] = (currentOptionValueIdx[lastOptionValueIdx] +
                                    (7 * (((scrollTotalDelta / 100) - currentIdx[idx]) / 7)) +
                                    (lastOptionValueIdx > idx).let { if (it) { 7 - lastOptionValueIdx + idx } else { idx - lastOptionValueIdx } }) %
                                    optionListSize
                        }

                        currentIdx[idx] = (7 + currentIdx[idx] - ((scrollTotalDelta / 100) % 7)) % 7
                        targetIdx[idx] = (7 + currentIdx[idx] - 1) % 7
                    }
                    // scroll downward
                    else {

                        if (currentIdx[idx] + (scrollTotalDelta / 100) > 6) {
                            currentOptionValueIdx[idx] = ((currentOptionValueIdx[startOptionValueIdx] -
                                    (7 * ((((scrollTotalDelta / 100) + currentIdx[idx]) / 7) - 1)) -
                                    (startOptionValueIdx < idx).let { if (it) { 7 - idx + startOptionValueIdx } else { startOptionValueIdx - idx } }) %
                                    optionListSize)
                                .let { if (it < 0) { it + optionListSize } else { it } }
                        }

                        currentIdx[idx] = (currentIdx[idx] + (scrollTotalDelta / 100)) % 7
                        targetIdx[idx] = (currentIdx[idx] + 1) % 7
                    }

                    reverseCurrentIdx[currentIdx[idx]] = idx
                }

                currentZIndex[idx] = calcCurrentIntValue(
                    initialZIndex[currentIdx[idx]],
                    initialZIndex[targetIdx[idx]],
                    scrollTotalDelta
                )

                currentAlpha[idx] = calcCurrentFloatValue(
                    initialAlpha[currentIdx[idx]],
                    initialAlpha[targetIdx[idx]],
                    scrollTotalDelta
                )

                currentScaleXY[idx] = calcCurrentFloatValue(
                    initialScaleXY[currentIdx[idx]],
                    initialScaleXY[targetIdx[idx]],
                    scrollTotalDelta
                )

                currentRotateX[idx] = calcCurrentFloatValue(
                    initialRotateX[currentIdx[idx]],
                    initialRotateX[targetIdx[idx]],
                    scrollTotalDelta
                )

                currentOffsetY[idx] = calcCurrentIntValue(
                    initialOffsetY[currentIdx[idx]],
                    initialOffsetY[targetIdx[idx]],
                    scrollTotalDelta
                )

                WheelPickerItem(
                    hazeState = hazeState,
                    suffix = suffix,
                    intensity = intensity,
                    currentZIndex = currentZIndex[idx],
                    currentScaleXY = currentScaleXY[idx],
                    currentRotateX = currentRotateX[idx],
                    currentOffsetY = currentOffsetY[idx],
                    currentIdx = currentIdx[idx],
                    targetIdx = targetIdx[idx],
                    currentAlpha = currentAlpha[idx],
                    optionValue = optionList[currentOptionValueIdx[idx]],
                    onConfirmation = { getOptionValue ->
                        onConfirm(getOptionValue)
                    }
                )
            }

            isUndo = false
            scrollDelta = 0
            scrollTotalDelta %= 100
        }
    }
}

@Composable
private fun WheelPickerItem(
    hazeState: HazeState,
    suffix: String,
    intensity: Float,
    currentZIndex: Int,
    currentScaleXY: Float,
    currentRotateX: Float,
    currentOffsetY: Int,
    currentIdx: Int,
    targetIdx: Int,
    currentAlpha: Float,
    optionValue: String,
    onConfirmation: (String) -> Unit
) {
    val currentColor = Color(
        ColorUtils.blendARGB(
            MaterialTheme.colorScheme.primary.toArgb(),
            MaterialTheme.colorScheme.onPrimary.toArgb(),
            currentAlpha
        )
    )
    val roundRectPointPos: (Float, Float, Boolean) -> Float = { initialPos, additionalPos, topLeft ->
        (initialPos) +
                (additionalPos
                    * currentScaleXY
                    * (currentIdx == 3).let { if (it) { currentScaleXY } else { 1f } }
                    * topLeft.let { if (it) { -1f } else { 1f } }
                )
    }
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .zIndex(currentZIndex.toFloat())
            .fillMaxWidth(0.75f)
            .height(100.dp)
            .offset {
                IntOffset(
                    0.dp.roundToPx(),
                    currentOffsetY.dp.roundToPx()
                )
            }
            .graphicsLayer {
                scaleX = currentScaleXY
                scaleY = scaleX
                rotationX = currentRotateX
            }
            .drawBehind {
                // item's outline
                drawOutline(
                    outline = Outline.Rounded(
                        roundRect = if (
                            currentIdx == 3
                            || targetIdx == 3
                        ) {
                            RoundRect(
                                roundRectPointPos(
                                    this.size.width * 0.25f,
                                    this.size.width * 0.3f,
                                    true
                                ),
                                roundRectPointPos(
                                    this.size.height * 0.5f,
                                    this.size.height * 0.6f,
                                    true
                                ),
                                roundRectPointPos(
                                    this.size.width * 0.75f,
                                    this.size.width * 0.3f,
                                    false
                                ),
                                roundRectPointPos(
                                    this.size.height * 0.5f,
                                    this.size.height * 0.6f,
                                    false
                                ),
                                CornerRadius(65f, 65f)
                            )
                        } else {
                            RoundRect(
                                0f, 0f, 0f, 0f,
                                CornerRadius(75f, 75f)
                            )
                        }
                    ),
                    color = currentColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20),
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            )
            .clip(shape = RoundedCornerShape(20))
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    backgroundColor = currentColor,
                    tint = HazeTint(color = onPrimary),
                    blurRadius = 25.dp,
                ),
                block = fun HazeEffectScope.() {
                    progressive =
                        HazeProgressive.LinearGradient(
                            startIntensity = intensity * currentAlpha,
                            endIntensity = intensity * currentAlpha,
                            preferPerformance = true
                        )
                })
            .clickable {
                if (currentIdx == 3) {
                    onConfirmation(optionValue)
                }
            }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = optionValue + suffix,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}