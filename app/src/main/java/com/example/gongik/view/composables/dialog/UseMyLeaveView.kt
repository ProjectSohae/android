package com.example.gongik.view.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
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

            Column {
                Text(
                    text = "휴가 종류",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column {
                Text(
                    text = "휴가 사유",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column {
                Text(
                    text = "휴가 기간",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column {
                Text(
                    text = "휴가 중 식비 지급 여부",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "미지급",
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "지급",
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column {
                Text(
                    text = "휴가 중 교통비 지급 여부",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "미지급",
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "지급",
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}