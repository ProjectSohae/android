package com.sohae.presentation.searchjob

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.textfield.CustomTextFieldView
import com.sohae.controller.mainnavgraph.MainNavGraphViewController

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
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onPrimary)
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
    val mainNavController = MainNavGraphViewController.mainNavController
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
                .clickable { mainNavController.popBackStack() },
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
                fontSize = 20.sp,
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
                    fontSize = 20.sp,
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
                        fontSize = 20.sp,
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