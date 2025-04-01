package com.sohae.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.CircularLoadingBarView
import com.sohae.common.ui.custom.composable.ProfileImage
import com.sohae.controller.ui.BarColorController
import com.sohae.domain.utils.getLeavePeriod
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(
    homeViewModel: HomeViewModel
){
    BarColorController.setNavigationBarColor(MaterialTheme.colorScheme.onPrimary)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        val leftPadding = it.calculateLeftPadding(LayoutDirection.Rtl)
        val rightPadding = it.calculateRightPadding(LayoutDirection.Rtl)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            item {
                HomeViewHeader(leftPadding, rightPadding)
            }

            item {
                HomeViewBody(
                    leftPadding,
                    rightPadding,
                    homeViewModel
                )
            }
        }
    }
}

@Composable
private fun HomeViewHeader(
    leftPadding : Dp,
    rightPadding : Dp
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            withTransform({
                translate(left = -(this.size.width / 24), top = this.size.height * 1.175f)
                scale(scaleX = this.size.width * 1.1f, scaleY = this.size.height)
            }) {
                drawCircle(
                    color = primary,
                    radius = 1.05f,
                    center = this.center
                )
            }
            withTransform({
                translate(left = -(this.size.width / 6f), top = this.size.height * 1.15f)
                scale(scaleX = this.size.width * 1.05f, scaleY = this.size.height)
            }) {
                drawCircle(
                    color = onPrimary,
                    radius = 1f,
                    center = this.center
                )
            }
            drawRect(
                color = onPrimary,
                size = Size(this.size.width, this.size.height * 2f),
                topLeft = Offset(0f, this.size.height)
            )
        }

        // profile image
        ProfileImage(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterStart)
                .offset(x = leftPadding + 16.dp, y = 48.dp)
                .shadow(4.dp, shape = RoundedCornerShape(100))
                .clickable {
                },
            innerPadding = PaddingValues(top = 8.dp),
            imageUrl = ""
        )

        // notification
        Box(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterEnd)
                .offset((-12).dp - rightPadding, 104.dp - rightPadding / 12f)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(100))
                .drawBehind { drawCircle(color = onPrimary) }
                .clickable {

                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_notifications_none_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .align(Alignment.Center),
                contentDescription = null
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeViewBody(
    leftPadding : Dp,
    rightPadding : Dp,
    homeViewModel: HomeViewModel
) {
    val myAccount = homeViewModel.myAccount.collectAsState().value
    val myWorkInfo = homeViewModel.myWorkInfo.collectAsState().value
    val myWelfare = homeViewModel.myWelfare.collectAsState().value
    val myRank = homeViewModel.myRank.collectAsState().value
    val myLeave = homeViewModel.myLeave.collectAsState().value
    var isReadyInfo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)

        if (!isReadyInfo) {
            isReadyInfo = true
        }
    }

    LaunchedEffect(
        myAccount,
        myWorkInfo,
        myWelfare,
        myRank,
        myLeave
    ) {
        if (
            myAccount != null
            && myWorkInfo != null
            && myWelfare != null
            && myRank != null
            && myLeave != null
        ) {
            isReadyInfo = true
        }
    }

    if (!isReadyInfo) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularLoadingBarView()
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(start = leftPadding, end = rightPadding)
        ) {
            MyDetails(homeViewModel)

            DateDetails(homeViewModel)
            Spacer(modifier = Modifier.size(12.dp))

            MySalary(homeViewModel)
            Spacer(modifier = Modifier.size(12.dp))

            MyLeaveList(homeViewModel)
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MyDetails(
    homeViewModel: HomeViewModel
) {
    val myInfo = homeViewModel.myAccount.collectAsState().value
    val myWorkInfo = homeViewModel.myWorkInfo.collectAsState().value

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = myInfo?.username ?: "로그인이 필요합니다.",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = myWorkInfo?.let {
                if (
                    it.startWorkDay < 0
                    || System.currentTimeMillis() < it.startWorkDay
                    )
                {
                    "직무 없음"
                } else { "사회복무요원" }
            } ?: "직무 없음",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = homeViewModel.getMyCurrentRank(System.currentTimeMillis()),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = myWorkInfo?.workPlace ?: "복무지 미정",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateDetails(
    homeViewModel: HomeViewModel
) {
    val myWorkInfo = homeViewModel.myWorkInfo.collectAsState().value
    val restWorkDays = if (myWorkInfo != null) {
        ChronoUnit.DAYS
            .between(
                LocalDateTime
                    .ofInstant(
                        Instant.ofEpochMilli(System.currentTimeMillis()),
                        ZoneId.systemDefault())
                    .toLocalDate(),
                LocalDateTime
                    .ofInstant(
                        Instant.ofEpochMilli(myWorkInfo.finishWorkDay),
                        ZoneId.systemDefault())
                    .toLocalDate()
            )
    } else {
        null
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "전체 근무일",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = myWorkInfo?.let {
                    if (it.startWorkDay < 0 || it.finishWorkDay < it.startWorkDay) {
                        "해당 없음"
                    } else {
                        "${((it.finishWorkDay - it.startWorkDay) / (1000 * 60 * 60 * 24)) + 1}일"
                    }
                } ?: "해당 없음",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "현재 근무일",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = myWorkInfo?.let {
                    if (it.startWorkDay < 0
                        || it.finishWorkDay < it.startWorkDay
                        || System.currentTimeMillis() < it.startWorkDay)
                    {
                        "해당 없음"
                    } else {
                        val lastTime = if (it.finishWorkDay < System.currentTimeMillis()) {
                            it.finishWorkDay
                        } else {
                            System.currentTimeMillis()
                        }

                        "${((lastTime - it.startWorkDay) / (1000 * 60 * 60 * 24)) + 1}일"
                    }
                } ?: "해당 없음",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "남은 근무일",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = myWorkInfo?.let {
                    if (
                        it.startWorkDay < 0
                        || System.currentTimeMillis() < it.startWorkDay
                        || it.finishWorkDay < it.startWorkDay
                        || it.finishWorkDay < System.currentTimeMillis())
                    {
                        "해당 없음"
                    } else {
                        "${restWorkDays}일"
                    }
                } ?: "해당 없음",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "다음 진급일",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = homeViewModel.getNextPromotionDay(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MySalary(
    homeViewModel: HomeViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val monthsCount = homeViewModel.monthsCount.collectAsState().value
    val salaryDetails = homeViewModel.salaryDetails.collectAsState().value
    var showSalaryDetails by remember { mutableStateOf(false) }

    LaunchedEffect(monthsCount) {
        homeViewModel.updateSalaryDetails()
    }

    if (showSalaryDetails) {

        if (salaryDetails.beginPayDate.isEqual(LocalDate.MIN)
            || salaryDetails.endPayDate.isEqual(LocalDate.MIN)) {
            com.sohae.controller.ui.snackbar.SnackBarController.show(
                inputMessage = "급여 정보가 존재하지 않습니다.",
                inputBehindTarget = com.sohae.controller.ui.snackbar.SnackBarBehindTarget.VIEW
            )
            showSalaryDetails = false
        } else {
            SalaryDetailsView(
                salaryDetails = salaryDetails,
                homeViewModel = homeViewModel,
                onDismissRequest = { showSalaryDetails = false }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20))
            .background(color = primary, shape = RoundedCornerShape(20))
            .clickable { showSalaryDetails = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.25f)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { homeViewModel.updateMonthsCount(monthsCount - 1) },
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = salaryDetails.resultDate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = salaryDetails.resultSalary.let {
                            if (it >= 0) {
                                com.sohae.domain.utils.displayAsAmount(it.toString())
                            } else { salaryDetails.errorMessage }
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    if (salaryDetails.resultSalary >= 0) {
                        Text(
                            text = "원",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { homeViewModel.updateMonthsCount(monthsCount + 1) },
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .background(color = secondary, shape = RoundedCornerShape(30))
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = onPrimary,
                        strokeWidth = (1.25f).dp.toPx(),
                        start = Offset(this.size.width / 2f, 12.dp.toPx()),
                        end = Offset(this.size.width / 2f, this.size.height - 12.dp.toPx())
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {

                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "외출 • 조퇴",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "0회",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "휴가",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "0회",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MyLeaveList(
    homeViewModel: HomeViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val myLeave = homeViewModel.myLeave.collectAsState().value
    val useLeaveItemsList = homeViewModel.leaveKindList
    val maxLeaveDaysList = listOf(
        myLeave?.firstAnnualLeave ?: 0,
        myLeave?.secondAnnualLeave ?: 0,
        myLeave?.sickLeave ?: 0
    )
    var selectedItemName by remember { mutableStateOf("휴가") }
    var showUsedMyLeaveListKindIdx by remember { mutableIntStateOf(-1) }
    var showUsingMyLeaveKindIdx by remember { mutableIntStateOf(-1) }

    if (showUsedMyLeaveListKindIdx >= 0) {
        MyUsedLeaveListView(
            title = "$selectedItemName 사용 기록",
            leaveKindIdx = showUsedMyLeaveListKindIdx,
            maxLeaveDaysList = maxLeaveDaysList,
            homeViewModel = homeViewModel,
            onDismissRequest = {
                homeViewModel.updateSalaryDetails()
                showUsedMyLeaveListKindIdx = -1
            }
        )
    }

    if (showUsingMyLeaveKindIdx >= 0) {
        UsingMyLeaveView(
            title = "$selectedItemName 사용",
            leaveKindIdx = showUsingMyLeaveKindIdx,
            leaveTypeList = homeViewModel.leaveTypeList[showUsingMyLeaveKindIdx],
            onDismissRequest = { showUsingMyLeaveKindIdx = -1 },
            onConfirm = { getMyUsedLeave ->

                if (showUsingMyLeaveKindIdx < 3) {
                    ((1000 * 60 * 60 * 8 * maxLeaveDaysList[showUsingMyLeaveKindIdx]) -
                            homeViewModel.getTotalUsedLeaveTime(showUsingMyLeaveKindIdx)).let {

                                if (it >= getMyUsedLeave.usedLeaveTime) {
                                    homeViewModel.takeMyLeave(getMyUsedLeave)
                                }
                                // 사용 하고자 하는 휴가 시간이 남은 휴가 시간을 넘어설 때
                                else {
                                    com.sohae.controller.ui.snackbar.SnackBarController.show(
                                        "${getLeavePeriod(it)}을 초과하여 휴가 사용할 수 없습니다.",
                                        com.sohae.controller.ui.snackbar.SnackBarBehindTarget.VIEW
                                    )
                                }
                    }
                } else {
                    homeViewModel.takeMyLeave(getMyUsedLeave)
                }

                homeViewModel.updateSalaryDetails()
                showUsingMyLeaveKindIdx = -1
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(10))
            .background(color = primary, shape = RoundedCornerShape(10)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .height(420.dp)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            useLeaveItemsList.forEachIndexed { idx, item ->
                UseLeaveItem(
                    itemName = item.first,
                    iconId = item.second,
                    leaveTime = if (idx < 3) {
                        homeViewModel.getRestLeaveTime(
                            leaveKindIdx = idx,
                            maxDays = maxLeaveDaysList[idx]
                        )
                    } else {
                        getLeavePeriod(
                            homeViewModel.getTotalUsedLeaveTime(
                                idx
                            )
                        )
                    },
                    suffix = if (idx < 3) {
                        "남음"
                    } else if (idx == 3) {
                        "사용"
                    } else {
                        "누적"
                    },
                    pressedButton = { getPressedButton ->

                        selectedItemName = item.first

                        // 0: 사용한 휴가 목록, 1: 휴가 사용
                        when (getPressedButton) {
                            0 -> { showUsedMyLeaveListKindIdx = idx }
                            1 -> { showUsingMyLeaveKindIdx = idx }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun UseLeaveItem(
    itemName: String,
    iconId: Int,
    leaveTime: String,
    suffix: String,
    pressedButton: (Int) -> Unit
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val secondary = MaterialTheme.colorScheme.secondary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(30))
            .background(color = secondary)
            .clickable { pressedButton(0) },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .drawBehind {
                            drawCircle(
                                color = primaryContainer,
                                radius = this.size.width
                            )
                        },
                    contentDescription = null
                )

                Column{
                    Text(
                        text = itemName,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = leaveTime,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            letterSpacing = (0.5f).sp,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Text(
                            text = suffix,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "사용",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .clickable { pressedButton(1) }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}