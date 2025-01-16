package com.example.gongik.view.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavGraphViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild

@Composable
fun UseMyLeaveView(
    onDismissRequest: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val leaveKind = listOf(
        "연차",
        "오전 반차", "오후 반차",
        "조퇴", "외출"
    )
    val welfareOptionsList = listOf("미지급", "지급")
    // 0: 미지급, 1: 지급
    var receiveLunchSupport by remember { mutableIntStateOf(0) }
    // 0: 미지급, 1: 지급
    var receiveTransportationSupport by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .shadow(20.dp, RoundedCornerShape(15))
                .clip(RoundedCornerShape(15))
                .hazeChild(
                    state = MainNavGraphViewModel.hazeState,
                    style = HazeStyle(
                        backgroundColor = MaterialTheme.colorScheme.onPrimary,
                        tint = HazeTint(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        blurRadius = 25.dp
                    )
                ) {
                    progressive = HazeProgressive.LinearGradient(
                        startIntensity = 0.85f,
                        endIntensity = 0.85f,
                        preferPerformance = true
                    )
                }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "휴가 사용",
                fontSize = dpToSp(dp = 20.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "휴가 종류",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                LazyRow {
                    item {
                        leaveKind.forEachIndexed { idx, item ->
                            Text(
                                text = item,
                                fontSize = dpToSp(dp = 16.dp),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .drawBehind {
                                        drawOutline(
                                            outline = Outline.Rounded(
                                                roundRect = RoundRect(
                                                    0f,
                                                    0f,
                                                    this.size.width,
                                                    this.size.height,
                                                    CornerRadius(100f, 100f)
                                                )
                                            ),
                                            color = tertiary,
                                            style = Stroke(width = (1.dp).toPx())
                                        )
                                    }
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "휴가 사유",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "휴가 기간",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "휴가 중 식비 지급 여부",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawRoundRect(
                                color = tertiary,
                                cornerRadius = CornerRadius(100f, 100f)
                            )
                            drawRoundRect(
                                topLeft = Offset(
                                    (this.size.width / 2f) * receiveLunchSupport.toFloat(),
                                    0f
                                ),
                                size = Size(
                                    this.size.width / 2f,
                                    this.size.height
                                ),
                                color = primary,
                                cornerRadius = CornerRadius(100f, 100f)
                            )
                        }
                        .padding(vertical = 8.dp)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    welfareOptionsList.forEachIndexed { idx, item ->
                        Text(
                            text = item,
                            fontSize = dpToSp(dp = 16.dp),
                            textAlign = TextAlign.Center,
                            color = if (idx == receiveLunchSupport) {
                                MaterialTheme.colorScheme.onPrimary
                            } else { MaterialTheme.colorScheme.primary },
                            modifier = Modifier
                                .weight(1f)
                                .clickable(indication = null, interactionSource = null) {
                                    receiveLunchSupport = idx
                                }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "휴가 중 교통비 지급 여부",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawRoundRect(
                                color = tertiary,
                                cornerRadius = CornerRadius(100f, 100f)
                            )
                            drawRoundRect(
                                topLeft = Offset(
                                    (this.size.width / 2f) * receiveTransportationSupport.toFloat(),
                                    0f
                                ),
                                size = Size(
                                    this.size.width / 2f,
                                    this.size.height
                                ),
                                color = primary,
                                cornerRadius = CornerRadius(100f, 100f)
                            )
                        }
                        .padding(vertical = 8.dp)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    welfareOptionsList.forEachIndexed { idx, item ->
                        Text(
                            text = item,
                            fontSize = dpToSp(dp = 16.dp),
                            textAlign = TextAlign.Center,
                            color = if (idx == receiveTransportationSupport) {
                                MaterialTheme.colorScheme.onPrimary
                            } else { MaterialTheme.colorScheme.primary },
                            modifier = Modifier
                                .weight(1f)
                                .clickable(indication = null, interactionSource = null) {
                                    receiveTransportationSupport = idx
                                }
                        )
                    }
                }
            }
        }
    }
}