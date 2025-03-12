package com.sohae.feature.jobnavgraph.selectdistrict

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.ColorUtils
import com.sohae.controller.mainnavgraph.MainScreenController
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun SelectDistrictView(
    content: List<String>,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val brightTertiary = Color(
        ColorUtils.blendARGB(
            MaterialTheme.colorScheme.tertiary.toArgb(),
            MaterialTheme.colorScheme.onPrimary.toArgb(),
            0.75f
        )
    )
    var selectedItemIdx by remember { mutableIntStateOf(-1) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .height(480.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(10)
                )
                .clip(RoundedCornerShape(10))
                .hazeEffect(
                    state = MainScreenController.hazeState,
                    style = HazeStyle(
                        backgroundColor = MaterialTheme.colorScheme.onPrimary,
                        tint = HazeTint(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        blurRadius = 25.dp
                    ),
                    block = fun HazeEffectScope.() {
                        progressive = HazeProgressive.LinearGradient(
                            startIntensity = 0.85f,
                            endIntensity = 0.85f,
                            preferPerformance = true
                        )
                    })
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "행정구역 선택",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = tertiary,
                            start = Offset(0f, this.size.height),
                            end = Offset(this.size.width, this.size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(
                    items = content,
                    key = { idx: Int, item: String -> idx }
                ) { idx: Int, item: String ->
                    Text(
                        text = item,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(this.size.width * 0.4f, 0f),
                                    end = Offset(this.size.width * 0.6f, 0f),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            .background(
                                color = if (selectedItemIdx == idx) {
                                    brightTertiary
                                } else { Color.Transparent }
                            )
                            .padding(vertical = 12.dp)
                            .clickable {

                                if (selectedItemIdx == idx) {
                                    selectedItemIdx = -1
                                } else {
                                    selectedItemIdx = idx
                                }
                            }
                    )
                }
            }

            Text(
                text = "확인",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = if (selectedItemIdx < 0) {
                    tertiary
                } else { MaterialTheme.colorScheme.primary },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = tertiary,
                            start = Offset(0f, 0f),
                            end = Offset(this.size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .clickable {

                        if (selectedItemIdx >= 0) {
                            onConfirm(content[selectedItemIdx])
                        }
                    }
                    .padding(vertical = 12.dp)
            )
        }
    }
}