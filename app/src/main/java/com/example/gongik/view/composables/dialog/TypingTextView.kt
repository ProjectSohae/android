package com.example.gongik.view.composables.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavController
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypingTextDialog(
    title: String,
    content: String,
    // placeholder, suffix
    inputsList: List<Pair<String, String>>,
    initialValuesList: List<String>,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )
    val inputsListSize = inputsList.size
    var inputText by rememberSaveable { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(10)
                )
                .clip(RoundedCornerShape(10))
                .hazeChild(
                    state = MainNavController.hazeState,
                    style = HazeStyle(
                        backgroundColor = MaterialTheme.colorScheme.onPrimary,
                        tint = HazeTint(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        blurRadius = 25.dp
                    )
                ) {
                    progressive = HazeProgressive.LinearGradient(
                        startIntensity = 0.9f,
                        endIntensity = 0.9f,
                        preferPerformance = true
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // dialog title 및 content
            Column(
                modifier = Modifier.padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(start = 48.dp, end = 48.dp),
                    text = title,
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(start = 44.dp, end = 44.dp),
                    text = content,
                    fontSize = dpToSp(dp = 12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(20.dp))

                // 텍스트 입력 필드들
                for (idx in 0..<inputsListSize) {
                    val interactionSource = remember { MutableInteractionSource() }

                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .offset(0.dp, (-idx).dp),
                        value = inputText,
                        onValueChange = { inputText = it },
                        enabled = true,
                        textStyle = TextStyle(
                            fontSize = dpToSp(dp = 16.dp),
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        interactionSource = interactionSource,
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = inputText,
                                visualTransformation = VisualTransformation.None,
                                innerTextField = innerTextField,
                                contentPadding = PaddingValues(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 6.dp,
                                    bottom = 6.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = inputsList[idx].first,
                                        fontSize = dpToSp(dp = 16.dp),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                },
                                suffix = {
                                    Text(
                                        text = inputsList[idx].second,
                                        fontSize = dpToSp(dp = 16.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                container = @Composable {
                                    Container(
                                        enabled = true,
                                        isError = false,
                                        interactionSource = interactionSource,
                                        modifier = Modifier,
                                        colors = textFieldColors,
                                        shape = RoundedCornerShape(
                                            topStartPercent = if (idx == 0) { 25 } else { 0 },
                                            topEndPercent = if (idx == 0) { 25 } else { 0 },
                                            bottomStartPercent = if (idx == inputsListSize - 1) { 25 } else { 0 },
                                            bottomEndPercent = if (idx == inputsListSize - 1) { 25 } else { 0 }
                                        ),
                                        focusedBorderThickness = 2.dp,
                                        unfocusedBorderThickness = 1.dp,
                                    )
                                },
                                singleLine = true,
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = textFieldColors
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))

            // 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = tertiary,
                            strokeWidth = 1f,
                            start = Offset.Zero,
                            end = Offset(this.size.width, 0f)
                        )
                    }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "확인",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onConfirmation("") }
                )
            }
        }
    }
}