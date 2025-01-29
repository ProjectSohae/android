package com.example.sohae.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.sohae.R
import com.example.sohae.util.font.dpToSp
import com.example.sohae.view.main.MainNavGraphViewModel
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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .width(260.dp)
                    .clip(RoundedCornerShape(25))
                    .background(Color(0xFFFEE500))
                    .clickable {

                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kakao_logo),
                    modifier = Modifier.size(18.dp),
                    contentDescription = null
                )

                Text(
                    text = "카카오 로그인",
                    fontWeight = FontWeight.Medium,
                    fontSize = dpToSp(dp = 14.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().alpha(0.85f)
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(260.dp)
                    .clip(RoundedCornerShape(25))
                    .background(Color(0xFFF2F2F2))
                    .clickable {

                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    modifier = Modifier.size(18.dp),
                    contentDescription = null
                )

                Text(
                    text = "구글 로그인",
                    fontWeight = FontWeight.Medium,
                    fontSize = dpToSp(dp = 14.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().alpha(0.54f)
                )
            }
        }
    }
}