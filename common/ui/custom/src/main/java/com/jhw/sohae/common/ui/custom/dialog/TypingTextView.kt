package com.jhw.sohae.common.ui.custom.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypingTextDialog(
    hazeState: HazeState,
    intensity: Float = 0.9f,
    title: String,
    content: String,
    // placeholder, suffix
    inputFormatsList: List<Pair<String, String>>,
    initialValuesList: List<String>,
    limitLength: Int = 20,
    isIntegerList: List<Boolean>,
    keyboardOptionsList: List<KeyboardOptions>,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
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
    val inputsListSize = inputFormatsList.size
    var inputTextsList by remember { mutableStateOf<List<TextFieldState>>(emptyList()) }
    val checkValuesChanged: () -> Boolean = {
        var result = false

        initialValuesList.forEachIndexed breaker@{ idx, item ->

            if (inputTextsList[idx].text != item) {
                result = true
                return@breaker
            }
        }

        result
    }

    LaunchedEffect(Unit) {
        val tmpList = mutableListOf<TextFieldState>()

        for (idx: Int in 0..<inputsListSize) {
            tmpList.add(
                TextFieldState(
                    initialText = isIntegerList[idx].let { isInt ->
                        if (isInt) {
                            initialValuesList[idx].toInt().let { getValue ->
                                if (getValue < 1){ "" } else { initialValuesList[idx] }
                            }
                        }
                        else { initialValuesList[idx] }
                    }
                )
            )
        }

        inputTextsList = tmpList
    }

    if (inputTextsList.isNotEmpty()) {
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
                        state = hazeState,
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
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // dialog title 및 content
                Column(
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(start = 48.dp, end = 48.dp),
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.padding(start = 36.dp, end = 36.dp, bottom = 20.dp),
                        text = content,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    // 텍스트 입력 필드들
                    for (idx in 0..<inputsListSize) {
                        val interactionSource = remember { MutableInteractionSource() }

                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .offset(0.dp, (-idx).dp),
                            state = inputTextsList[idx],
                            inputTransformation = {

                                if (isIntegerList[idx]) {

                                    if (
                                        // 현재 입력 버퍼를 CharSequence로 반환받는다.
                                        // 반환 받은 버퍼에서 숫자 아닌 값이 존재하는지 확인한다.
                                        asCharSequence().any { !it.isDigit() }
                                        || length > 5
                                    ) { revertAllChanges() }
                                } else {

                                    if (length > limitLength) { revertAllChanges() }
                                }
                            },
                            outputTransformation = {
                                var inputLength = length - 3

                                if (isIntegerList[idx]) {
                                    while (inputLength > 0) {
                                        insert(inputLength, ",")
                                        inputLength -= 3
                                    }
                                }
                            },
                            enabled = true,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            keyboardOptions = keyboardOptionsList[idx],
                            interactionSource = interactionSource,
                            lineLimits = TextFieldLineLimits.SingleLine,
                            decorator = { innerTextField ->
                                OutlinedTextFieldDefaults.DecorationBox(
                                    value = inputTextsList[idx].text.toString(),
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
                                            text = inputFormatsList[idx].first,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.tertiary,
                                        )
                                    },
                                    suffix = {
                                        Text(
                                            text = inputFormatsList[idx].second,
                                            fontSize = 16.sp,
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = if (checkValuesChanged()) {
                            MaterialTheme.colorScheme.primary
                        } else { tertiary },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (checkValuesChanged()) {
                                    onConfirmation(inputTextsList[0].text.toString())
                                }
                            }
                    )
                }
            }
        }
    }
}