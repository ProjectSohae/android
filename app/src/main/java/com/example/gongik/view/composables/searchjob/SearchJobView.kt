package com.example.gongik.view.composables.searchjob

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavGraphViewModel
import com.example.gongik.view.composables.snackbar.SnackBarBehindTarget
import com.example.gongik.view.composables.snackbar.SnackBarController

@Composable
fun SearchJobView(
    searchJobViewModel: SearchJobViewModel = viewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        val innerPadding = PaddingValues(
            it.calculateLeftPadding(LayoutDirection.Rtl),
            it.calculateTopPadding(),
            it.calculateRightPadding(LayoutDirection.Rtl),
            0.dp
        )

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchJobViewHeader(searchJobViewModel)

            SearchJobViewBody(searchJobViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchJobViewHeader(
    searchJobViewModel: SearchJobViewModel
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
    val searchPostTitle = searchJobViewModel.searchJobName.collectAsState().value
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedContainerColor = brightTertiary,
        unfocusedContainerColor = brightTertiary,
        errorContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )

    Row(
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
            .padding(start = 12.dp, end = 24.dp, top = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { MainNavGraphViewModel.popBack() },
            contentDescription = null
        )

        BasicTextField(
            modifier = Modifier.weight(1f),
            value = searchPostTitle,
            onValueChange = {
                searchJobViewModel.updateSearchJobName(it)
            },
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
                    value = searchPostTitle,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    contentPadding = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    placeholder = {
                        Text(
                            text = "복무지 검색",
                            fontSize = dpToSp(dp = 16.dp),
                            color = primary,)
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
                            tint = if (searchPostTitle.isEmpty()) {
                                Color.Transparent
                            } else { tertiary },
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = null
                                ) {
                                    if (searchPostTitle.isNotEmpty()) {
                                        searchJobViewModel.updateSearchJobName("")
                                    }
                                }
                        )
                    }
                )
            }
        )
    }
}

@Composable
private fun SearchJobViewBody(
    searchJobViewModel: SearchJobViewModel
) {
    // 검색어 일치 복무지 목록
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
        }
    }
}