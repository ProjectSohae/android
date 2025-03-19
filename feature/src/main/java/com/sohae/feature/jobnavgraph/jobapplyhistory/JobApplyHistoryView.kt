package com.sohae.feature.jobnavgraph.jobapplyhistory

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sohae.common.ui.custom.dialog.WheelPickerDialog
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.feature.jobnavgraph.option.SelectDistrictView

// 이전 경쟁률
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobApplyHistoryView(
    jobApplyHistoryViewModel: JobApplyHistoryViewModel = hiltViewModel()
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    var selectedFilterIdx by rememberSaveable { mutableIntStateOf(-1) }
    var jeopsu_yy by rememberSaveable { mutableStateOf("") }
    var jeopsu_tms by rememberSaveable { mutableStateOf("") }
    var ghjbc_cd by rememberSaveable { mutableStateOf("") }
    var bjdsggjuso_cd by rememberSaveable { mutableStateOf("") }
    var isReadyBjdsggjusoCd by rememberSaveable { mutableStateOf(false) }
    var loadJobApplyHistoryList by rememberSaveable { mutableStateOf(false) }
    val filterOptionsList = listOf(
        jobApplyHistoryViewModel.jeopsu_yy.keys.toList(),
        jobApplyHistoryViewModel.jeopsu_tms.keys.toList(),
        jobApplyHistoryViewModel.ghjbc_cd.keys.toList()
    )
    val getItemIdx: (String) -> Int = {
        var tmp = 0

        filterOptionsList[selectedFilterIdx].forEachIndexed { idx, item ->
            if (it == item) { tmp = idx }
        }

        tmp
    }
    val checkAllFilterSelected: () -> Boolean = {

        if (jeopsu_yy.isBlank()
            || jeopsu_tms.isBlank()
            || ghjbc_cd.isBlank()
            || bjdsggjuso_cd.isBlank())
        {
            false
        } else { true }
    }

    LaunchedEffect(loadJobApplyHistoryList) {

        if (loadJobApplyHistoryList) {
            jobApplyHistoryViewModel.updateJobApplyHistoryList(emptyList())
            jobApplyHistoryViewModel.getJobApplyHistoryList(
                jobApplyHistoryViewModel.jeopsu_yy[jeopsu_yy] ?: "",
                jobApplyHistoryViewModel.jeopsu_tms[jeopsu_tms] ?: "",
                jobApplyHistoryViewModel.ghjbc_cd[ghjbc_cd] ?: "",
                jobApplyHistoryViewModel.bjdsggjuso_cd[bjdsggjuso_cd] ?: "",
                callback = {
                    SnackBarController.show(it, SnackBarBehindTarget.VIEW)
                }
            )
        }

        loadJobApplyHistoryList = false
    }

    if (selectedFilterIdx >= 0) {

        if (selectedFilterIdx < 3) {
            WheelPickerDialog(
                hazeState = MainScreenController.hazeState,
                initIdx = when (selectedFilterIdx) {
                    0 -> {
                        getItemIdx(jeopsu_yy)
                    }

                    1 -> {
                        getItemIdx(jeopsu_tms)
                    }

                    2 -> {
                        getItemIdx(ghjbc_cd)
                    }

                    else -> {
                        0
                    }
                },
                intensity = 0.85f,
                onDismissRequest = { selectedFilterIdx = -1 },
                onConfirm = { getSelectedValue ->

                    when (selectedFilterIdx) {
                        // 접수 년도
                        0 -> {
                            jeopsu_yy = getSelectedValue.toString()
                        }
                        // 회차
                        1 -> {
                            jeopsu_tms = getSelectedValue.toString()
                        }
                        // 관할 지방청
                        2 -> {
                            ghjbc_cd = getSelectedValue.toString()
                            bjdsggjuso_cd = ""

                            jobApplyHistoryViewModel.getBjdsggjusoCd(
                                ghjbc_cd = jobApplyHistoryViewModel.ghjbc_cd[ghjbc_cd] ?: "",
                                callback = { errorMessage ->

                                    if (errorMessage.isBlank()) {
                                        isReadyBjdsggjusoCd = true
                                    } else {
                                        SnackBarController.show(
                                            errorMessage,
                                            SnackBarBehindTarget.VIEW
                                        )
                                    }
                                }
                            )
                        }
                    }

                    selectedFilterIdx = -1

                    if (checkAllFilterSelected()) {
                        loadJobApplyHistoryList = true
                    }
                },
                optionList = filterOptionsList[selectedFilterIdx]
            )
        }
        else {

            if (ghjbc_cd.isBlank()) {
                SnackBarController.show("관할 지방청을 선택해주세요.", SnackBarBehindTarget.VIEW)
                selectedFilterIdx = -1
            } else {

                if (jobApplyHistoryViewModel.bjdsggjuso_cd.isEmpty()) {

                    if (isReadyBjdsggjusoCd) {
                        SnackBarController.show("데이터가 존재하지 않습니다.", SnackBarBehindTarget.VIEW)
                    } else {
                        SnackBarController.show("데이터를 불러오고 있습니다.\n잠시 후 다시 시도해주세요.", SnackBarBehindTarget.VIEW)
                    }

                    selectedFilterIdx = -1
                } else {
                    SelectDistrictView(
                        content = jobApplyHistoryViewModel.bjdsggjuso_cd.keys.toList(),
                        onDismissRequest = { selectedFilterIdx = -1 },
                        onConfirm = {
                            bjdsggjuso_cd = it
                            selectedFilterIdx = -1

                            if (checkAllFilterSelected()) {
                                loadJobApplyHistoryList = true
                            }
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
            // 접수 년도, 회차, 관할 지방청, 시.군.구 필터
            LazyRow(
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
                    .padding(vertical = 12.dp)
            ) {
                // 접수 년도
                item {
                    JobSearchFilterItem(
                        modifier = Modifier.padding(start = 16.dp),
                        filterName = "접수 년도",
                        filterValue = jeopsu_yy,
                        pressFilterItem = { selectedFilterIdx = 0 }
                    )
                }

                // 재학생 입영원, 본인 선택
                item {
                    JobSearchFilterItem(
                        filterName = "회차",
                        filterValue = jeopsu_tms,
                        pressFilterItem = { selectedFilterIdx = 1 }
                    )
                }

                // 관할 지방청
                item {
                    JobSearchFilterItem(
                        filterName = "관할 지방청",
                        filterValue = ghjbc_cd,
                        pressFilterItem = { selectedFilterIdx = 2 }
                    )
                }

                // 시.군.구
                item {
                    JobSearchFilterItem(
                        filterName = "시 • 군 • 구",
                        filterValue = bjdsggjuso_cd,
                        pressFilterItem = { selectedFilterIdx = 3 }
                    )
                }
            }

            val jobApplyHistoryList = jobApplyHistoryViewModel.jobApplyHistoryList.collectAsState().value
            var pressedItemIdx by rememberSaveable { mutableIntStateOf(-1) }

            if (
                jeopsu_yy.isBlank()
                || jeopsu_tms.isBlank()
                || ghjbc_cd.isBlank()
                || bjdsggjuso_cd.isBlank()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 72.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "접수 년도, 회차, 관할 지방청, 시 • 군 • 구를 선택해 주세요.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    item {
                        Column(modifier = Modifier.width(3600.dp)) {
                            JobDetailsCategoryView(jobApplyHistoryViewModel)

                            if (jobApplyHistoryList.isEmpty()) {

                            } else {
                                LazyColumn {
                                    itemsIndexed(
                                        items = jobApplyHistoryList,
                                        key = { index: Int, item ->
                                            index
                                        }
                                    ) { index: Int, item ->
                                        JobApplyHistoryItem(
                                            jobApplyHistory = item,
                                            drawOverline = (index > 0),
                                            isPressed = (index == pressedItemIdx),
                                            jobApplyHistoryViewModel = jobApplyHistoryViewModel,
                                            pressItem = {
                                                pressedItemIdx = if (index == pressedItemIdx) {
                                                    -1
                                                } else { index }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
private fun JobDetailsCategoryView(
    jobApplyHistoryViewModel: JobApplyHistoryViewModel
) {
    val primary = MaterialTheme.colorScheme.primary

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            jobApplyHistoryViewModel.jobDetailsFirstCategory.forEachIndexed { idx: Int, item ->
                Text(
                    text = item.first,
                    fontSize = 16.sp,
                    color = primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(item.second)
                        .drawBehind {
                            if (item.first.isNotBlank()) {
                                drawLine(
                                    color = primary,
                                    start = Offset(0f, this.size.height),
                                    end = Offset(this.size.width, this.size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                                drawLine(
                                    color = primary,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, this.size.height * 2f),
                                    strokeWidth = 1.dp.toPx()
                                )

                                if (idx == 4) {
                                    drawLine(
                                        color = primary,
                                        start = Offset(this.size.width, 0f),
                                        end = Offset(this.size.width, this.size.height * 2f),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                            }
                        }
                        .padding(vertical = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = primary,
                        strokeWidth = 2.dp.toPx(),
                        start = Offset(0f, this.size.height),
                        end = Offset(this.size.width, this.size.height)
                    )
                }
                .padding(vertical = 8.dp)
        ) {
            jobApplyHistoryViewModel.jobDetailsSecondCategory.forEach { item ->
                Text(
                    text = item.first,
                    fontSize = 16.sp,
                    color = primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(item.second)
                )
            }
        }
    }
}

@Composable
private fun JobApplyHistoryItem(
    jobApplyHistory: List<String>,
    drawOverline: Boolean,
    isPressed: Boolean,
    jobApplyHistoryViewModel: JobApplyHistoryViewModel,
    pressItem: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isPressed) {
                    primary
                } else {
                    onPrimary
                }
            )
            .clickable { pressItem() }
            .drawBehind {
                if (drawOverline) {
                    drawLine(
                        color = primary,
                        start = Offset(0f, 0f),
                        end = Offset(this.size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        jobApplyHistory.forEachIndexed { index: Int, item ->
            Text(
                text = item,
                fontSize = 16.sp,
                color = if (isPressed) { onPrimary } else { primary },
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(jobApplyHistoryViewModel.jobDetailsSecondCategory[index].second)
            )
        }
    }
}