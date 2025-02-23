package com.sohae.common.ui.custom.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.sohae.common.ui.custom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFieldView(
    modifier: Modifier,
    value: String = "",
    placeholder: String = "",
    contentPadding: PaddingValues,
    onValueChange: (String) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val brightTertiary = Color(
        ColorUtils.blendARGB(
            MaterialTheme.colorScheme.tertiary.toArgb(),
            MaterialTheme.colorScheme.onPrimary.toArgb(),
            0.75f
        )
    )
    val interactionSource = remember { MutableInteractionSource() }
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedContainerColor = brightTertiary,
        unfocusedContainerColor = brightTertiary,
        errorContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )

    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        enabled = true,
        textStyle = TextStyle(
            fontSize = 16.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        interactionSource = interactionSource,
        singleLine = true,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                contentPadding = contentPadding,
                placeholder = {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = primary
                    )
                },
                container = @Composable {
                    Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        modifier = Modifier,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(25)
                    )
                },
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = textFieldColors,
                suffix = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                        tint = if (value.isEmpty()) {
                            Color.Transparent
                        } else { tertiary },
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                if (value.isNotEmpty()) { onValueChange("") }
                            }
                    )
                }
            )
        }
    )
}