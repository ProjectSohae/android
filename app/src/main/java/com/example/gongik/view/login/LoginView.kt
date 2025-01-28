package com.example.gongik.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.main.MainNavGraphViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild

@Composable
fun LoginView(
    intensity: Float = 0.85f,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(10)
                )
                .clip(RoundedCornerShape(10))
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
                        startIntensity = intensity,
                        endIntensity = intensity,
                        preferPerformance = true
                    )
                }
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "로그인",
                fontWeight = FontWeight.SemiBold,
                fontSize = dpToSp(dp = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "카카오",
                fontSize = dpToSp(dp = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .background(Color.Yellow)
                    .padding(vertical = 12.dp)
            )

            Text(
                text = "구글",
                fontSize = dpToSp(dp = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .background(Color.Red)
                    .padding(vertical = 12.dp)
            )
        }
    }
}