package com.jhw.sohae.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jhw.sohae.common.resource.R
import com.jhw.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.jhw.sohae.common.ui.custom.snackbar.SnackBarController
import com.jhw.sohae.common.ui.custom.snackbar.SnackbarView
import com.jhw.sohae.controller.mainnavgraph.MainScreenController
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.utils.getDate
import com.jhw.utils.getLeavePeriod
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyUsedLeaveListView(
    title: String,
    leaveKindIdx: Int,
    maxLeaveDaysList: List<Int>,
    homeViewModel: HomeViewModel,
    onDismissRequest: () -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    var loadMyUsedLeaveList by remember { mutableStateOf(true) }
    var myUsedLeaveList: List<MyUsedLeaveEntity> by remember { mutableStateOf(emptyList()) }
    var selectedLeaveItem: MyUsedLeaveEntity? by remember { mutableStateOf(null) }
    var deleteLeaveItemId by remember { mutableIntStateOf(-1) }

    LaunchedEffect(loadMyUsedLeaveList) {

        if (loadMyUsedLeaveList) {
            myUsedLeaveList = homeViewModel.getMyUsedLeaveListByLeaveKindIdx(leaveKindIdx)
            loadMyUsedLeaveList = false
        }
    }

    LaunchedEffect(deleteLeaveItemId) {

        if (deleteLeaveItemId >= 0) {
            homeViewModel.deleteMyUsedLeave(deleteLeaveItemId)
            deleteLeaveItemId = -1
            loadMyUsedLeaveList = true
        }
    }

    if (selectedLeaveItem != null) {
        UsingMyLeaveView(
            myUsedLeave = selectedLeaveItem,
            title = "${homeViewModel.leaveKindList[selectedLeaveItem!!.leaveKindIdx].first} 사용",
            leaveKindIdx = selectedLeaveItem!!.leaveKindIdx,
            leaveTypeList = homeViewModel.leaveTypeList[selectedLeaveItem!!.leaveKindIdx],
            onDismissRequest = { selectedLeaveItem = null },
            onConfirm = { getMyUsedLeave ->

                if (getMyUsedLeave.leaveKindIdx < 3) {
                    ((1000 * 60 * 60 * 8 * maxLeaveDaysList[getMyUsedLeave.leaveKindIdx]) -
                            homeViewModel.getTotalUsedLeaveTime(getMyUsedLeave.leaveKindIdx)).let {

                            if (it + selectedLeaveItem!!.usedLeaveTime >= getMyUsedLeave.usedLeaveTime) {
                                homeViewModel.takeMyLeave(getMyUsedLeave)
                            }
                            // 사용 하고자 하는 휴가 시간이 남은 휴가 시간을 넘어설 때
                            else {
                                SnackBarController.show(
                                    "${getLeavePeriod(it + selectedLeaveItem!!.usedLeaveTime)}을 초과하여 휴가 사용할 수 없습니다.",
                                    SnackBarBehindTarget.DIALOG
                                )
                            }
                    }
                } else {
                    homeViewModel.takeMyLeave(getMyUsedLeave)
                }

                loadMyUsedLeaveList = true
                selectedLeaveItem = null
            }
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = SnackBarController.snackbarHostState
                ) { getSnackbarData ->
                    if (SnackBarController.behindTarget == SnackBarBehindTarget.DIALOG) {
                        SnackBarController.currentSnackbar = getSnackbarData
                        SnackbarView(snackbarData = getSnackbarData)
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight()
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { onDismissRequest() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(540.dp)
                        .shadow(12.dp, RoundedCornerShape(10))
                        .clip(RoundedCornerShape(10))
                        .hazeEffect(state = MainScreenController.hazeState,
                            style = HazeStyle(
                                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                                tint = HazeTint(
                                    color = MaterialTheme.colorScheme.onPrimary
                                ),
                                blurRadius = 25.dp
                            ),
                            block = fun HazeEffectScope.() {
                                progressive = HazeProgressive.LinearGradient(
                                    startIntensity = 0.9f,
                                    endIntensity = 0.9f,
                                    preferPerformance = true
                                )
                            })
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) {},
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(0f, this.size.height),
                                    end = Offset(this.size.width, this.size.height),
                                    strokeWidth = (1.dp).toPx()
                                )
                            }
                            .padding(vertical = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        itemsIndexed(
                            items = myUsedLeaveList,
                            key = { idx: Int, item: MyUsedLeaveEntity -> idx }
                        ) { idx: Int, item: MyUsedLeaveEntity ->
                            MyUsedLeaveListItem(
                                item = item,
                                modifier = if (idx == 0) {
                                    Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                                } else {
                                    Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                                },
                                homeViewModel = homeViewModel,
                                pressedButtonIdx = { pressedButtonIdx ->

                                    when (pressedButtonIdx) {
                                        0 -> { selectedLeaveItem = item }
                                        1 -> { deleteLeaveItemId = item.id }
                                    }
                                }
                            )
                        }
                    }

                    Text(
                        text = "닫기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(0f, 0f),
                                    end = Offset(this.size.width, 0f),
                                    strokeWidth = (1.dp).toPx()
                                )
                            }
                            .padding(vertical = 12.dp)
                            .clickable { onDismissRequest() }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MyUsedLeaveListItem(
    item: MyUsedLeaveEntity,
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    pressedButtonIdx: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { pressedButtonIdx(0) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 휴가 종류
            Text(
                text = homeViewModel.leaveTypeList[item.leaveKindIdx][item.leaveTypeIdx],
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            // 휴가 시간
            Text(
                text = "${getLeavePeriod(item.usedLeaveTime)} 사용",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // 휴가 사유
        Text(
            text = item.reason.let {
                it.ifBlank { "휴가 사유 없음" }
            },
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1
        )

        // 휴가 사용 일정
        Text(
            text = getDate(item.leaveStartDate) + item.leaveEndDate.let {
                if (it > 0) { " - ${getDate(it)}" } else { "" }
            },
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow {
                item {
                    // 식비 지급 여부
                    Text(
                        text = item.receiveLunchSupport.let {
                            if (it) { "식비 지급" } else { "식비 미지급" }
                        },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(100)
                            )
                            .padding(horizontal = 12.dp)
                    )

                    // 교통비 지급 여부
                    Text(
                        text = item.receiveTransportationSupport.let {
                            if (it) { "교통비 지급" } else { "교통비 미지급" }
                        },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(100)
                            )
                            .padding(horizontal = 12.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.outline_delete_24),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null,
                modifier = Modifier.clickable { pressedButtonIdx(1) }
            )
        }
    }
}