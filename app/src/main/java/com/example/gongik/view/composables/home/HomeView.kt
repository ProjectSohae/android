package com.example.gongik.view.composables.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.controller.BarColorController
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.dialog.UseMyLeaveView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel()
){
    BarColorController.setNavigationBarColor(MaterialTheme.colorScheme.onPrimary)

    Scaffold {
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
        Surface(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterStart)
                .offset(x = leftPadding + 16.dp, y = 48.dp)
                .shadow(4.dp, shape = RoundedCornerShape(100))
                .clickable(
                    indication = null,
                    interactionSource = null
                ) {
                },
            color = onPrimary,
            shape = RoundedCornerShape(100)
        ) {
            if (false) {

            }
            else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_profile_basic_icon_24),
                    tint = primary,
                    modifier = Modifier.offset(y = 8.dp),
                    contentDescription = null
                )
            }
        }

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
    val isReadyInfo = homeViewModel.isReadyInfo.collectAsState().value
    val finishLoadDB = homeViewModel.finishLoadDB.collectAsState().value

    if (!isReadyInfo) {
        Text(text = "test")
        
        if (finishLoadDB) {
            Text(text = "리로드")
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

            MyVacations(homeViewModel)
            Spacer(modifier = Modifier.size(12.dp))

            UseLeave(homeViewModel)
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun MyDetails(
    homeViewModel: HomeViewModel
) {
    val myInfo = homeViewModel.myInformation.collectAsState().value!!
    val myWorkInfo = homeViewModel.myWorkInformation.collectAsState().value!!
    val myRank = homeViewModel.myRank.collectAsState().value!!

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = myInfo.nickname.let {
                if (it.isBlank()) { "로그인이 필요합니다." }
                else { myInfo.nickname }
            },
            fontSize = dpToSp(dp = 32.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = myWorkInfo.startWorkDay.let {
                if (it < 0 || System.currentTimeMillis() < it) { "직무 없음" } else { "사회복무요원" }
            },
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = homeViewModel.getMyCurrentRank(),
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = myWorkInfo.workPlace.let {
                if (it.isBlank()) { "복무지 미정" } else { myWorkInfo.workPlace }
            },
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DateDetails(
    homeViewModel: HomeViewModel
) {
    val myWorkInfo = homeViewModel.myWorkInformation.collectAsState().value!!

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
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = myWorkInfo.startWorkDay.let {
                    if (it < 0 || myWorkInfo.finishWorkDay < it) {
                        "해당 없음"
                    } else {
                        "${((myWorkInfo.finishWorkDay - it) / (1000 * 60 * 60 * 24)) + 1}일"
                    }
                },
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = myWorkInfo.startWorkDay.let {
                    if (it < 0
                        || myWorkInfo.finishWorkDay < it
                        || System.currentTimeMillis() < it)
                    {
                        "해당 없음"
                    } else {
                        val lastTime = if (myWorkInfo.finishWorkDay < System.currentTimeMillis()) {
                            myWorkInfo.finishWorkDay
                        } else {
                            System.currentTimeMillis()
                        }

                        "${((lastTime - it) / (1000 * 60 * 60 * 24)) + 1}일"
                    }
                },
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = myWorkInfo.finishWorkDay.let {
                    if (
                        myWorkInfo.startWorkDay < 0
                        || System.currentTimeMillis() < myWorkInfo.startWorkDay
                        || it < myWorkInfo.startWorkDay
                        || it < System.currentTimeMillis())
                    {
                        "해당 없음"
                    } else {
                        "${((myWorkInfo.finishWorkDay - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)) + 1}일"
                    }
                },
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = homeViewModel.getNextPromotionDay(),
                fontSize = dpToSp(dp = 12.dp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
private fun MyVacations(
    homeViewModel: HomeViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20))
            .background(color = primary, shape = RoundedCornerShape(20)),
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
                    ) {

                    },
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                homeViewModel.getMyCurrentSalary(0).let { getResult ->
                    Text(
                        text = getResult.first,
                        fontSize = dpToSp(dp = 16.dp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getResult.second,
                            fontSize = dpToSp(dp = 32.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        if (getResult.first.isNotBlank()) {
                            Text(
                                text = "원",
                                fontSize = dpToSp(dp = 24.dp),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
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
                    ) {

                    },
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
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "0회",
                    fontSize = dpToSp(dp = 20.dp),
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
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "0회",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun UseLeave(
    homeViewModel: HomeViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val myLeave = homeViewModel.myLeave.collectAsState().value!!
    val useVacationItemsList = homeViewModel.useVacationItemsList
    val maxLeaveDaysList = listOf(
        myLeave.firstAnnualLeave,
        myLeave.secondAnnualLeave,
        myLeave.sickLeave
    )
    var showUsedMyLeaveList by remember { mutableIntStateOf(-1) }
    var showUsingMyLeave by remember { mutableIntStateOf(-1) }

    if (showUsedMyLeaveList >= 0) {

    }

    if (showUsingMyLeave >= 0) {
        UseMyLeaveView(
            onDismissRequest = { showUsingMyLeave = -1 }
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
            useVacationItemsList.forEachIndexed { idx, item ->
                UseLeaveItem(
                    itemName = item.first,
                    iconId = item.second,
                    leaveTime = if (idx < 3) {
                        homeViewModel.getRestLeaveTime(
                            0,
                            maxLeaveDaysList[idx]
                        )
                    } else {
                        homeViewModel.getUsedLeaveTime(60 * 8 + 70)
                    },
                    suffix = if (idx < 3) {
                        "남음"
                    } else if (idx < 4) {
                        "사용"
                    } else {
                        "누적"
                    },
                    pressedButton = { getPressedButton ->

                        when (getPressedButton) {
                            0 -> { showUsedMyLeaveList = idx }
                            1 -> { showUsingMyLeave = idx }
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
                        fontSize = dpToSp(dp = 12.dp)
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
                            letterSpacing = dpToSp(dp = (0.5f).dp),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dpToSp(dp = 16.dp)
                        )

                        Text(
                            text = suffix,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            fontSize = dpToSp(dp = 14.dp),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "사용",
                fontSize = dpToSp(dp = 16.dp),
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