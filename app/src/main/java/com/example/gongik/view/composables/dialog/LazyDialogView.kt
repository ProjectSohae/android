package com.example.gongik.view.composables.dialog

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavController
import com.example.gongik.view.composables.main.MainNavGraphView
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild
import kotlin.math.abs

@Composable
fun LazySelectDialog(
    onDissmissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    title: String,
    optionsList: List<String>
) {
    val inputListSize = optionsList.size
    var scrollTotalDelta by remember { mutableIntStateOf(0) }
    var scrollDelta = 0
    var scrollDir = 0
    var isUndo = false
    val initialZIndexes = listOf( 0, 100, 200, 300, 200, 100, 0 )
    val initialAlpha = listOf( 0.55f, 0.65f, 0.8f, 1f, 0.8f, 0.65f, 0.55f)
    val initialScaleXY = listOf( 0.5f, 0.6f, 0.8f, 1f, 0.8f, 0.6f, 0.5f )
    val initialOffsetY = listOf( 8, -24, 20, 70, 156, 260, 276)
    val currentZIndexes = mutableListOf( 0, 100, 200, 300, 200, 100, 0 )
    val currentScaleXY = mutableListOf( 0.5f, 0.6f, 0.8f, 1f, 0.8f, 0.6f, 0.5f )
    val currentOffsetY = mutableListOf( 8, -24, 20, 70, 156, 260, 276)
    val currentAlpha = mutableListOf( 0.55f, 0.65f, 0.8f, 1f, 0.8f, 0.65f, 0.55f)
    val currentIdx = mutableListOf( 0, 1, 2, 3, 4, 5, 6 )
    val targetIdx = mutableListOf( 0, 1, 2, 3, 4, 5, 6 )

    Dialog(
        onDismissRequest = { onDissmissRequest() }
    ) {
        val primary = MaterialTheme.colorScheme.primary
        val onPrimary = MaterialTheme.colorScheme.onPrimary
        val tertiary = MaterialTheme.colorScheme.tertiary

        Log.d("currentOffset", "${scrollDelta} ${scrollTotalDelta}")
        if (scrollDelta == 0) {
            Log.d("currentOffset", "end")
        }

        Box(
            modifier = Modifier
                .width(240.dp)
                .height(240.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    // + <- 아래로 스크롤, - <- 위로 스크롤
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
                        }
                        else {
                            isUndo = false
                        }

                        scrollTotalDelta += abs(scrollDelta)
                        delta
                    },
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            for (idx: Int in 0..6) {

                if (isUndo) { currentIdx[idx] = targetIdx[idx] }

                if (abs(scrollDelta) > 0) {

                    // 위로 스크롤
                    if (scrollDelta < 0) {

                        currentIdx[idx] = (7 + currentIdx[idx] - ((scrollTotalDelta / 100) % 7)) % 7
                        targetIdx[idx] = (7 + currentIdx[idx] - 1) % 7

                        currentZIndexes[idx] = initialZIndexes[currentIdx[idx]] +
                                (
                                        (initialZIndexes[targetIdx[idx]] - initialZIndexes[currentIdx[idx]])
                                                * (scrollTotalDelta % 100)
                                        ) / 100

                        currentAlpha[idx] = initialAlpha[currentIdx[idx]] +
                                (
                                        (initialAlpha[targetIdx[idx]] - initialAlpha[currentIdx[idx]])
                                                * (scrollTotalDelta % 100)
                                        ) / 100f

                        currentScaleXY[idx] = initialScaleXY[currentIdx[idx]] +
                                (
                                        (initialScaleXY[targetIdx[idx]] - initialScaleXY[currentIdx[idx]])
                                                * ((scrollTotalDelta % 100).toFloat() / 100f)
                                        )

                        currentOffsetY[idx] = initialOffsetY[currentIdx[idx]] +
                                (
                                        (initialOffsetY[targetIdx[idx]] - initialOffsetY[currentIdx[idx]])
                                                * (scrollTotalDelta % 100)
                                        ) / 100
                    }
                    // 아래로 스크롤
                    else {

                        currentIdx[idx] = (currentIdx[idx] + (scrollTotalDelta / 100)) % 7
                        targetIdx[idx] = (currentIdx[idx] + 1) % 7

                        currentZIndexes[idx] = initialZIndexes[currentIdx[idx]] +
                                (
                                        (initialZIndexes[targetIdx[idx]] - initialZIndexes[currentIdx[idx]])
                                                * (scrollTotalDelta % 100)
                                        ) / 100

                        currentAlpha[idx] = initialAlpha[currentIdx[idx]] +
                                (
                                        (initialAlpha[targetIdx[idx]] - initialAlpha[currentIdx[idx]])
                                                * (scrollTotalDelta % 100)
                                        ) / 100f

                        currentScaleXY[idx] = initialScaleXY[currentIdx[idx]] +
                                (
                                        (initialScaleXY[targetIdx[idx]] - initialScaleXY[currentIdx[idx]])
                                                * ((scrollTotalDelta % 100).toFloat() / 100f)
                                        )

                        currentOffsetY[idx] = initialOffsetY[currentIdx[idx]] +
                                (
                                        (initialOffsetY[targetIdx[idx]] - initialOffsetY[currentIdx[idx]])
                                                * (scrollTotalDelta % 100)
                                        ) / 100
                    }
                }

                Column(
                    modifier = Modifier
                        .zIndex(currentZIndexes[idx].toFloat())
                        .width(240.dp)
                        .height(100.dp)
                        .graphicsLayer {
                            scaleX = currentScaleXY[idx]
                            scaleY = scaleX
                        }
                        .offset {
                            IntOffset(
                                0.dp.roundToPx(),
                                currentOffsetY[idx].dp.roundToPx()
                            )
                        }
                        .drawBehind {
                            drawOutline(
                                outline = Outline.Rounded(
                                    roundRect = if (
                                        currentIdx[idx] == 3
                                        || targetIdx[idx] == 3
                                    ) {
                                        RoundRect(
                                            (this.size.width * 0.25f)
                                                    - (this.size.width * 0.3f * currentScaleXY[idx]
                                                    * (currentIdx[idx] == 3).let {
                                                        if (it) { currentScaleXY[idx] } else { 1f }
                                                    }),
                                            (this.size.height * 0.5f)
                                                    - (this.size.height * 0.6f * currentScaleXY[idx]
                                                    * (currentIdx[idx] == 3).let {
                                                        if (it) { currentScaleXY[idx] } else { 1f }
                                                    }),
                                            (this.size.width * 0.75f)
                                                    + (this.size.width * 0.3f * currentScaleXY[idx]
                                                    * (currentIdx[idx] == 3).let {
                                                        if (it) { currentScaleXY[idx] } else { 1f }
                                                    }),
                                            (this.size.height * 0.5f)
                                                    + (this.size.height * 0.6f * currentScaleXY[idx]
                                                    * (currentIdx[idx] == 3).let {
                                                        if (it) { currentScaleXY[idx] } else { 1f }
                                                    }),
                                            CornerRadius(65f, 65f)
                                        )
                                    }
                                    else {
                                        RoundRect(
                                            0f, 0f, 0f, 0f,
                                            CornerRadius(75f, 75f)
                                        )
                                    }
                                ),
                                color = onPrimary,
                                style = Stroke(width = 10f)
                            )
                        }
                        .shadow(
                            elevation = 20.dp,
                            ambientColor = MaterialTheme.colorScheme.primary,
                            spotColor = MaterialTheme.colorScheme.primary
                        )
                        .clip(shape = RoundedCornerShape(20))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .graphicsLayer { alpha = currentAlpha[idx] }
                        .hazeChild(
                            state = MainNavController.hazeState,
                            style = HazeStyle(
                                backgroundColor = onPrimary,
                                tint = HazeTint(color = onPrimary),
                                blurRadius = 25.dp,
                            )
                        ) {
                            progressive =
                                HazeProgressive.LinearGradient(
                                    start = Offset.Zero,
                                    startIntensity = 0.6f,
                                    end = Offset.Infinite,
                                    endIntensity = 0.6f,
                                    preferPerformance = true
                                )
                        }
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = optionsList[idx % inputListSize],
                        fontSize = dpToSp(dp = 20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clickable {

                                if (currentIdx[idx] == 3) {
                                    onConfirmation(optionsList[idx % inputListSize],)
                                }
                            }
                    )
                }
            }

            isUndo = false
            scrollTotalDelta %= 100
        }
    }
}