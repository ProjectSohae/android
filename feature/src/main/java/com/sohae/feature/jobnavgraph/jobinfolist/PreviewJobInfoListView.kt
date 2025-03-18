package com.sohae.feature.jobnavgraph.jobinfolist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.dialog.WheelPickerDialog
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.feature.jobnavgraph.option.SelectDistrictView

// 복무지 목록
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviewJobInfoListView(
    previewJobInfoListViewModel: PreviewJobInfoListViewModel = hiltViewModel()
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val tertiary = MaterialTheme.colorScheme.tertiary
    val previewJobList = listOf(1, 2, 3, 4, 5,)
    var ghjbc_cd by rememberSaveable { mutableStateOf("") }
    var bjdsggjuso_cd by rememberSaveable { mutableStateOf("") }
    var isReadyBjdsggjusoCd by rememberSaveable { mutableStateOf(false) }
    var pressedFilterIdx by rememberSaveable { mutableIntStateOf(-1) }
    var sortBy by rememberSaveable { mutableStateOf(previewJobInfoListViewModel.sortByList[0]) }
    val filterOptionsList = listOf(
        previewJobInfoListViewModel.ghjbc_cd.keys.toList(),
        emptyList(),
        previewJobInfoListViewModel.sortByList
    )
    val getItemIdx: (String, List<String>) -> Int = { value, optionsList ->
        var tmp = 0

        optionsList.forEachIndexed breaker@{ index, optionValue ->
            if (optionValue == value) {
                tmp = index
                return@breaker
            }
        }

        tmp
    }

    if (pressedFilterIdx >= 0) {

        // 관할 지방청, 별점순 정렬
        if (pressedFilterIdx == 0 || pressedFilterIdx == 2) {
            WheelPickerDialog(
                hazeState = MainScreenController.hazeState,
                initIdx = when (pressedFilterIdx) {
                    0 -> {
                        getItemIdx(ghjbc_cd, filterOptionsList[pressedFilterIdx])
                    }

                    2 -> {
                        getItemIdx(sortBy, filterOptionsList[pressedFilterIdx])
                    }

                    else -> {
                        0
                    }
                },
                intensity = 0.8f,
                onDismissRequest = { pressedFilterIdx = -1 },
                onConfirmation = { getOptionValue ->

                    when (pressedFilterIdx) {
                        0 -> {
                            isReadyBjdsggjusoCd = false
                            ghjbc_cd = getOptionValue.toString()

                            previewJobInfoListViewModel.getBjdsggjusoCd(
                                ghjbc_cd = previewJobInfoListViewModel.ghjbc_cd[ghjbc_cd] ?: "",
                                callback = { errorMessage ->

                                    if (errorMessage.isBlank()) {
                                        isReadyBjdsggjusoCd = true
                                    } else {
                                        SnackBarController.show(errorMessage, SnackBarBehindTarget.VIEW)
                                    }
                                }
                            )
                        }

                        2 -> {
                            sortBy = getOptionValue.toString()
                        }
                    }

                    pressedFilterIdx = -1
                },
                optionsList = filterOptionsList[pressedFilterIdx]
            )
        }
        // 시.군.구
        else {

            if (ghjbc_cd.isBlank()) {
                SnackBarController.show("관할 지방청을 선택해주세요.", SnackBarBehindTarget.VIEW)
                pressedFilterIdx = -1
            } else {

                if (previewJobInfoListViewModel.bjdsggjuso_cd.isEmpty()) {

                    if (isReadyBjdsggjusoCd) {
                        SnackBarController.show("데이터가 존재하지 않습니다.", SnackBarBehindTarget.VIEW)
                    } else {
                        SnackBarController.show("데이터를 불러오고 있습니다.\n잠시 후 다시 시도해주세요.", SnackBarBehindTarget.VIEW)
                    }

                    pressedFilterIdx = -1
                } else {
                    SelectDistrictView(
                        content = previewJobInfoListViewModel.bjdsggjuso_cd.keys.toList(),
                        onDismissRequest = { pressedFilterIdx = -1 },
                        onConfirm = {
                            bjdsggjuso_cd = it
                            pressedFilterIdx = -1
                        }
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 복무지 검색 필터
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = tertiary,
                            strokeWidth = 1.dp.toPx(),
                            start = Offset(0f, this.size.height),
                            end = Offset(this.size.width, this.size.height)
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        JobSearchFilterItem(
                            filterName = "관할 지방청",
                            filterValue = ghjbc_cd,
                            pressFilterItem = { pressedFilterIdx = 0 }
                        )
                    }

                    item {
                        JobSearchFilterItem(
                            filterName = "시 • 군 • 구",
                            filterValue = bjdsggjuso_cd,
                            pressFilterItem = { pressedFilterIdx = 1 }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable { pressedFilterIdx = 2 },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 인기순, 리뷰 많은 순 정렬
                    Text(
                        text = sortBy,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.outline_sort_24),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }

            // 복무지 목록
            if (ghjbc_cd.isBlank() || bjdsggjuso_cd.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "관할 지방청, 시 • 군 • 구를\n선택해 주세요.",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onPrimary),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(
                        items = previewJobList,
                        key = { index: Int, previewPost ->
                            index
                        }
                    ) { index: Int, previewPost ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            PreviewJobItem()

                            // 광고
                            if (index % 2 == 0) {
                                Surface(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .fillParentMaxWidth()
                                        .height(120.dp)
                                ) {

                                }
                            }
                        }
                    }
                }
            }
        }

        // 리뷰 쓰기
        Surface(
            modifier = Modifier
                .size(48.dp)
                .offset(x = (-12).dp, y = (-12).dp)
                .clip(RoundedCornerShape(100))
                .clickable {
                    mainNavController.navigate(MainNavGraphRoutes.WRITEJOBREVIEW.name)
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
private fun JobSearchFilterItem(
    modifier: Modifier = Modifier,
    filterName: String,
    filterValue: String,
    pressFilterItem: () -> Unit
) {
    Text(
        text = filterValue.ifBlank { filterName },
        fontSize = 16.sp,
        color = filterValue.let {
            if (it.isBlank()) {
                MaterialTheme.colorScheme.tertiary
            } else { MaterialTheme.colorScheme.onPrimary }
        },
        modifier = modifier
            .padding(end = 12.dp)
            .border(
                width = 1.dp,
                color = filterValue.let {
                    if (it.isBlank()) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Color.Transparent
                    }
                },
                shape = RoundedCornerShape(100)
            )
            .background(
                color = filterValue.let {
                    if (it.isBlank()) {
                        Color.Transparent
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                },
                shape = RoundedCornerShape(100)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { pressFilterItem() }
    )
}

@Composable
private fun PreviewJobItem() {
    val mainNavController = MainNavGraphViewController.mainNavController
    val tertiary = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawOutline(
                    outline = Outline.Rounded(
                        roundRect = RoundRect(
                            left = 0f,
                            right = this.size.width,
                            top = 0f,
                            bottom = this.size.height,
                            cornerRadius = CornerRadius(10f, 10f)
                        )
                    ),
                    color = tertiary,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            .clickable {
                mainNavController.navigate(MainNavGraphRoutes.JOBINFORMATION.name)
            }
            .padding(16.dp)
    ) {
        Text(
            text = "복무지",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "복무 분야",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "복무지 주소",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                tint = Color(0xFFFDCC0D),
                modifier = Modifier.size(16.dp),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "4.0",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}