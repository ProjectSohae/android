package com.sohae.feature.profile

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.CircularLoadingBarView
import com.sohae.common.ui.custom.composable.ProfileImage
import com.sohae.common.ui.custom.dialog.DatePickerDialog
import com.sohae.common.ui.custom.dialog.TypingTextDialog
import com.sohae.common.ui.custom.dialog.WheelPickerDialog
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.domain.utils.displayAsAmount
import com.sohae.domain.utils.getDate
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel
) {
    val myAccount = profileViewModel.myAccount.collectAsState().value
    val myWorkInfo = profileViewModel.myWorkInfo.collectAsState().value
    val myWelfare = profileViewModel.myWelfare.collectAsState().value
    val myRank = profileViewModel.myRank.collectAsState().value
    val myLeave = profileViewModel.myLeave.collectAsState().value
    var isReadyInfo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000L)

        isReadyInfo = true
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        val innerPadding = PaddingValues(
            it.calculateLeftPadding(LayoutDirection.Rtl),
            it.calculateTopPadding(),
            it.calculateRightPadding(LayoutDirection.Rtl),
            0.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (!isReadyInfo) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoadingBarView()
                }
            }
            else {
                ProfileViewHeader()

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 프로필
                    item {
                        PreviewProfileDetails(profileViewModel)
                    }

                    // 나의 활동
                    item {
                        MyActivities(profileViewModel)
                    }

                    // 복무일
                    item {
                        MilitaryServiceDate(profileViewModel)
                    }

                    // 진급일
                    item {
                        PromotionDate(profileViewModel)
                    }

                    // 복지
                    item {
                        SalaryDetails(profileViewModel)
                    }

                    // 휴가 일수
                    item {
                        RestTimeDetails(profileViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileViewHeader(

) {
    val mainNavController = MainNavGraphViewController.mainNavController

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "프로필",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Row {
            // setting
            Icon(
                painter = painterResource(id = R.drawable.outline_settings_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        mainNavController.navigate(MainNavGraphRoutes.SETTINGOPTIONS.name)
                    },
                contentDescription = null
            )
        }
    }
}

// 프로필
@Composable
private fun PreviewProfileDetails(
    profileViewModel: ProfileViewModel
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val myAccount = profileViewModel.myAccount.collectAsState().value
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                mainNavController.navigate(MainNavGraphRoutes.MYPROFILE.name)
            }
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    cornerRadius = CornerRadius(x = 50f, y = 50f)
                )
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(64.dp),
                innerPadding = PaddingValues(top = 4.dp)
            )

            Column {
                Text(
                    text = myAccount?.username ?: "로그인이 필요합니다.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = myAccount?.emailAddress ?: "이메일 없음",
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
    }
}

// 내가 작성한 글, 댓글
@Composable
private fun MyActivities(
    profileViewModel: ProfileViewModel
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val itemList = listOf(
        Pair("내가 작성한 글", MainNavGraphRoutes.MYPOSTLIST.name),
        Pair("내가 작성한 댓글", MainNavGraphRoutes.MYCOMMENTLIST.name),
        Pair("내가 작성한 복무지 리뷰", MainNavGraphRoutes.MYJOBREVIEWLIST.name),
    )
    val primary = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding()
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    cornerRadius = CornerRadius(50f, 50f)
                )
            }
            .padding(16.dp)
    ) {
        Text(
            text = "나의 활동",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        itemList.forEachIndexed { idx: Int, item ->
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .clickable {
                        mainNavController.navigate(item.second)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.first,
                    fontSize = 16.sp
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// 복무 정보
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MilitaryServiceDate(
    profileViewModel: ProfileViewModel
) {
    val myWorkInfo = profileViewModel.myWorkInfo.collectAsState().value
    val primary = MaterialTheme.colorScheme.primary
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {

        if (openDialog < 1) {

        }
        else {
            DatePickerDialog(
                title = when (openDialog) {
                    1 -> {
                        "소집일 선택"
                    }

                    2 -> {
                        "소집 해제일 선택"
                    }

                    else -> {
                        "날짜 선택"
                    }
                },
                initialSelectedDateMillis = when (openDialog) {
                    1 -> {
                        myWorkInfo?.startWorkDay ?: -1
                    }

                    2 -> {
                        myWorkInfo?.finishWorkDay ?: -1
                    }

                    else -> {
                        -1
                    }
                },
                onDateSelected = { getSelectedDate ->

                    if (getSelectedDate != null) {
                        profileViewModel.updateMyWorkInfo(openDialog, getSelectedDate)
                    }

                    openDialog = -1
                },
                onDismissRequest = {
                    openDialog = -1
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    cornerRadius = CornerRadius(50f, 50f)
                )
            }
            .padding(16.dp)
    ) {
        Text(
            text = "복무 정보",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 0 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "복무지",
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWorkInfo?.workPlace ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 1 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "소집일", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWorkInfo?.let {
                        if (it.startWorkDay < 0) { "해당 없음" } else {
                            getDate(it.startWorkDay)
                        }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDialog = 2 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "소집 해제일", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWorkInfo?.let {
                        if (it.finishWorkDay < 0) { "해당 없음" } else {
                            getDate(it.finishWorkDay)
                        }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// 진급일
@Composable
private fun PromotionDate(
    profileViewModel: ProfileViewModel
) {
    val myRank = profileViewModel.myRank.collectAsState().value
    val primary = MaterialTheme.colorScheme.primary
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {
        DatePickerDialog(
            title = when (openDialog) {
                0 -> {
                    "일등병 진급일 선택"
                }

                1 -> {
                    "상등병 진급일 선택"
                }

                2 -> {
                    "병장 진급일 선택"
                }

                else -> {
                    "진급일 선택"
                }
            },
            initialSelectedDateMillis = when (openDialog) {
                0 -> {
                    myRank?.firstPromotionDay ?: -1
                }

                1 -> {
                    myRank?.secondPromotionDay ?: -1
                }

                2 -> {
                    myRank?.thirdPromotionDay ?: -1
                }

                else -> {
                    -1
                }
            },
            onDateSelected = { getSelectedDate ->

                if (getSelectedDate != null) {
                    profileViewModel.updateMyRank(openDialog, getSelectedDate)
                }

                openDialog = -1
            },
            onDismissRequest = {
                openDialog = -1
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    cornerRadius = CornerRadius(50f, 50f)
                )
            }
            .padding(16.dp)
    ) {
        Text(
            text = "진급일",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 0 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "일등병 진급일",
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myRank?.let {
                        if (it.firstPromotionDay < 0) { "해당 없음" } else {
                            getDate(it.firstPromotionDay)
                        }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 1 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "상등병 진급일",
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myRank?.let {
                        if (it.secondPromotionDay < 0) { "해당 없음" } else {
                            getDate(it.secondPromotionDay)
                        }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDialog = 2 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "병장 진급일",
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myRank?.let {
                        if (it.thirdPromotionDay < 0) { "해당 없음" } else {
                            getDate(it.thirdPromotionDay)
                        }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// 복지
@Composable
private fun SalaryDetails(
    profileViewModel: ProfileViewModel
) {
    val myWelfare = profileViewModel.myWelfare.collectAsState().value
    val primary = MaterialTheme.colorScheme.primary
    val title = listOf("식비", "교통비",)
    val content = listOf(
        "지원 받는 식비 액수를 입력 하세요.",
        "지원 받는 교통비 액수를 입력 하세요."
    )
    val inputForm = listOf(
        listOf(Pair("금액 입력", "원")),
        listOf(Pair("금액 입력", "원"))
    )
    val isIntegerList = listOf(
        listOf(true),
        listOf(true)
    )
    val keyboardOptionsList = listOf(
        listOf(
            KeyboardOptions(keyboardType = KeyboardType.Decimal)
        ),
        listOf(
            KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    )
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {

        if (openDialog != 2) {
            TypingTextDialog(
                hazeState = MainScreenController.hazeState,
                title = title[openDialog],
                content = content[openDialog],
                inputFormatsList = inputForm[openDialog],
                initialValuesList = openDialog.let {
                    when (it) {
                        0 -> {
                            listOf(myWelfare?.lunchSupport?.toString() ?: "0")
                        }

                        1 -> {
                            listOf(myWelfare?.transportationSupport?.toString() ?: "0")
                        }

                        else -> {
                            listOf("")
                        }
                    }
                },
                isIntegerList = isIntegerList[openDialog],
                keyboardOptionsList = keyboardOptionsList[openDialog],
                onDismissRequest = { openDialog = -1 },
                onConfirmation = { getValue ->

                    if (getValue.isNotBlank()) {
                        profileViewModel.updateMyWelfare(
                            openDialog,
                            getValue.toInt()
                        )
                    }

                    openDialog = -1
                }
            )
        }
        else {
            WheelPickerDialog(
                hazeState = MainScreenController.hazeState,
                initIdx = myWelfare?.let {
                    if (it.payday > 0) {
                        it.payday - 1
                    } else { 0 }
                } ?: 0,
                suffix = "일",
                intensity = 1f,
                onDismissRequest = { openDialog = -1 },
                onConfirmation = { getValue ->
                    profileViewModel.updateMyWelfare(
                        openDialog,
                        getValue.toString().toInt()
                    )
                    openDialog = -1
                },
                optionsList = profileViewModel.startPayDayList
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    cornerRadius = CornerRadius(50f, 50f)
                )
            }
            .padding(16.dp)
    ) {
        Text(
            text = "복지",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 0 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "식비", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWelfare?.let {
                        if (it.lunchSupport < 1) {
                            "해당 없음"
                        } else { "${displayAsAmount(it.lunchSupport.toString())}원" }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 1 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "교통비", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWelfare?.let {
                        if (it.transportationSupport < 1) {
                            "해당 없음"
                        } else { "${displayAsAmount(it.transportationSupport.toString())}원" }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDialog = 2 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "월급 계산 시작일", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWelfare?.let {
                        if (it.payday < 1) {
                            "해당 없음"
                        } else { "${it.payday}일" }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// 휴가 일수
@Composable
private fun RestTimeDetails(
    profileViewModel: ProfileViewModel
) {
    val myLeave = profileViewModel.myLeave.collectAsState().value
    val primary = MaterialTheme.colorScheme.primary
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {
        WheelPickerDialog(
            hazeState = MainScreenController.hazeState,
            initIdx = when (openDialog) {
                0 -> {
                    myLeave?.let {
                        if (it.firstAnnualLeave > 0) {
                            it.firstAnnualLeave - 1
                        } else {
                            0
                        }
                    } ?: 0
                }

                1 -> {
                    myLeave?.let {
                        if (it.secondAnnualLeave > 0) {
                            it.secondAnnualLeave - 1
                        } else {
                            0
                        }
                    } ?: 0
                }

                2 -> {
                    myLeave?.let {
                        if (it.sickLeave > 0) {
                            it.sickLeave - 1
                        } else {
                            0
                        }
                    } ?: 0
                }

                else -> {
                    0
                }
            },
            suffix = "일",
            intensity = 0.95f,
            onDismissRequest = { openDialog = -1 },
            onConfirmation = { getDays ->
                profileViewModel.updateMyLeave(
                    openDialog,
                    getDays.toString().toInt()
                )
                openDialog = -1
            },
            optionsList = profileViewModel.leaveDayList
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    cornerRadius = CornerRadius(50f, 50f)
                )
            }
            .padding(16.dp)
    ) {
        Text(
            text = "휴가 일수",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 0 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "1년차 연차", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myLeave?.let {
                        if (it.firstAnnualLeave < 0) {
                            "해당 없음"
                        } else { "${it.firstAnnualLeave}일" }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable { openDialog = 1 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "2년차 연차", fontSize = 16.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myLeave?.let {
                        if (it.secondAnnualLeave < 0) {
                            "해당 없음"
                        } else { "${it.secondAnnualLeave}일" }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDialog = 2 },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "병가",
                fontSize = 16.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myLeave?.let {
                        if (it.sickLeave < 0) {
                            "해당 없음"
                        } else { "${it.sickLeave}일" }
                    } ?: "해당 없음",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}