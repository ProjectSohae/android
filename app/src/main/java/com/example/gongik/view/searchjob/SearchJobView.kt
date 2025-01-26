package com.example.gongik.view.searchjob

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.textfield.CustomTextFieldView
import com.example.gongik.view.main.MainNavGraphViewModel

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

@Composable
private fun SearchJobViewHeader(
    searchJobViewModel: SearchJobViewModel
) {
    val searchJobName = searchJobViewModel.searchJobName.collectAsState().value
    val tertiary = MaterialTheme.colorScheme.tertiary

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

        CustomTextFieldView(
            modifier = Modifier.weight(1f),
            value = searchJobName,
            placeholder = "복무지 검색",
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            onValueChange = {
                searchJobViewModel.updateSearchJobName(it)
            }
        )
    }
}

@Composable
private fun SearchJobViewBody(
    searchJobViewModel: SearchJobViewModel
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val searchJobName = searchJobViewModel.searchJobName.collectAsState().value
    val searchJobList = searchJobViewModel.searchJobList.collectAsState().value

    if (searchJobName.isBlank()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "찾고자 하는 복무지를 입력해주세요.",
                fontWeight = FontWeight.Medium,
                fontSize = dpToSp(dp = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    else {
        // 검색어 일치 복무지 목록
        if (searchJobList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "검색 결과가 존재하지 않습니다.",
                    fontWeight = FontWeight.Medium,
                    fontSize = dpToSp(dp = 20.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = searchJobList,
                    key = { idx: Int, item: String -> idx }
                ) { idx: Int, item: String ->

                    Text(
                        text = item,
                        fontSize = dpToSp(dp = 20.dp),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .drawBehind {
                                if (idx > 0) {
                                    drawLine(
                                        color = tertiary,
                                        start = Offset(0f, 0f),
                                        end = Offset(this.size.width, 0f),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                            }
                            .clickable {

                            }
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}