package com.sohae.feature.post.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sohae.controller.ui.MainScreenController
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun PostOptionView(
    isMyPost: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val itemModifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            drawLine(
                color = tertiary,
                start = Offset(0f, this.size.height),
                end = Offset(this.size.width, this.size.height),
                strokeWidth = (1.dp).toPx()
            )
        }
        .padding(vertical = 12.dp)

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .width(240.dp)
                .clip(RoundedCornerShape(25))
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
                .padding(horizontal = 24.dp)
        ) {

            // 타인 게시글
            if (!isMyPost) {
                Text(
                    text = "작성자 차단",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = itemModifier.clickable { onConfirm(0) }
                )

                Text(
                    text = "게시글 신고",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                    modifier = itemModifier.clickable { onConfirm(1) }
                )
            }
            // 내 게시글
            else {
                Text(
                    text = "게시글 수정",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = itemModifier.clickable { onConfirm(2) }
                )

                Text(
                    text = "게시글 삭제",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = itemModifier.clickable { onConfirm(3) }
                )
            }
        }
    }
}