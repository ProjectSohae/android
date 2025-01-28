package com.example.gongik.view.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.util.font.dpToSp
import com.example.gongik.util.function.displayAsAmount
import com.example.gongik.util.function.getDate
import com.example.gongik.view.composables.dialog.DatePickerDialog
import com.example.gongik.view.composables.dialog.TypingTextDialog
import com.example.gongik.view.composables.dialog.WheelPickerDialog
import com.example.gongik.view.main.MainNavGraphItems
import com.example.gongik.view.main.MainNavGraphViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel = viewModel()
) {
    val finishLoadDB = profileViewModel.finishLoadDB.collectAsState().value
    val isReadyInfo = profileViewModel.isReadyInfo.collectAsState().value

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
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (!isReadyInfo) {
                Text(text = "test")

                if (finishLoadDB) {
                    Text(text = "리로드")
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "프로필",
            fontSize = dpToSp(dp = 24.dp),
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
                        MainNavGraphViewModel.navigate(MainNavGraphItems.SETTINGOPTIONS.name)
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
    val myInfo = profileViewModel.myInformation.collectAsState().value!!
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                MainNavGraphViewModel.navigate(MainNavGraphItems.MYPROFILE.name)
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
                modifier = Modifier.padding(end = 12.dp).size(64.dp),
                innerPadding = PaddingValues(top = 4.dp)
            )

            Column {
                Text(
                    text = myInfo.nickname.let {
                        if (it.isBlank()) { "로그인이 필요합니다." }
                        else { myInfo.nickname }
                    },
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = myInfo.emailAddress.let {
                        if (it.isBlank()) { "이메일 없음" }
                        else { myInfo.nickname }
                    },
                    fontSize = dpToSp(dp = 16.dp)
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

@Composable
fun ProfileImage(
    modifier: Modifier,
    innerPadding: PaddingValues,
    imageUrl: String = ""
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(100))
    ) {
        if (imageUrl.isBlank()) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_profile_basic_icon_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(innerPadding),
                contentDescription = null
            )
        } else {

        }
    }
}

// 내가 작성한 글, 댓글
@Composable
private fun MyActivities(
    profileViewModel: ProfileViewModel
) {
    val primary = MaterialTheme.colorScheme.primary

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
            text = "나의 활동",
            fontWeight = FontWeight.SemiBold,
            fontSize = dpToSp(dp = 20.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .clickable {

                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내가 작성한 글",
                fontSize = dpToSp(dp = 16.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내가 작성한 댓글",
                fontSize = dpToSp(dp = 16.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// 복무 정보
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MilitaryServiceDate(
    profileViewModel: ProfileViewModel
) {
    val myWorkInfo = profileViewModel.myWorkInformation.collectAsState().value!!
    val primary = MaterialTheme.colorScheme.primary
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {

        if (openDialog < 1) {

        }
        else {
            DatePickerDialog(
                title = when (openDialog) {
                    1 -> { "소집일 선택" }
                    2 -> { "소집 해제일 선택" }
                    else -> { "날짜 선택" }
                },
                initialSelectedDateMillis = when (openDialog) {
                    1 -> { myWorkInfo.startWorkDay }
                    2 -> { myWorkInfo.finishWorkDay }
                    else -> { -1 }
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
            fontSize = dpToSp(dp = 20.dp),
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
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWorkInfo.workPlace.let {
                        if (it.isBlank()) { "해당 없음" } else { myWorkInfo.workPlace }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                text = "소집일",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWorkInfo.startWorkDay.let {
                        if (it < 0) { "해당 없음" } else { getDate(it) }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                text = "소집 해제일",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWorkInfo.finishWorkDay.let {
                        if (it < 0) { "해당 없음" } else { getDate(it) }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
    val myRank = profileViewModel.myRank.collectAsState().value!!
    val primary = MaterialTheme.colorScheme.primary
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {
        DatePickerDialog(
            title = when (openDialog) {
                0 -> { "일등병 진급일 선택" }
                1 -> { "상등병 진급일 선택" }
                2 -> { "병장 진급일 선택" }
                else -> { "진급일 선택" }
            },
            initialSelectedDateMillis = when (openDialog) {
                0 -> { myRank.firstPromotionDay }
                1 -> { myRank.secondPromotionDay }
                2 -> { myRank.thirdPromotionDay }
                else -> { -1 }
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
            fontSize = dpToSp(dp = 20.dp),
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
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myRank.firstPromotionDay.let {
                        if (it < 0) { "해당 없음" } else { getDate(it) }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myRank.secondPromotionDay.let {
                        if (it < 0) { "해당 없음" } else { getDate(it) }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myRank.thirdPromotionDay.let {
                        if (it < 0) { "해당 없음" } else { getDate(it) }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
    val myWelfare = profileViewModel.myWelfare.collectAsState().value!!
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
                title = title[openDialog],
                content = content[openDialog],
                inputFormatsList = inputForm[openDialog],
                initialValuesList = openDialog.let {
                    when (it) {
                        0 -> { listOf( myWelfare.lunchSupport.toString() ) }
                        1 -> { listOf( myWelfare.transportationSupport.toString() ) }
                        else -> { listOf( "" ) }
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
                initIdx = if (myWelfare.payday > 0) {
                    myWelfare.payday - 1
                } else { 0 },
                suffix = "일",
                intensity = 1f,
                onDismissRequest = { openDialog = -1 },
                onConfirmation = { getValue ->
                    profileViewModel.updateMyWelfare(
                        openDialog,
                        getValue.toString().toInt()
                    )
                    openDialog = - 1
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
            fontSize = dpToSp(dp = 20.dp),
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
                text = "식비",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWelfare.lunchSupport.let {
                        if (it < 1) { "해당 없음" } else { "${displayAsAmount(it.toString())}원" }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                text = "교통비",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWelfare.transportationSupport.let {
                        if (it < 1) { "해당 없음" } else { "${displayAsAmount(it.toString())}원" }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                text = "월급 계산 시작일",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myWelfare.payday.let {
                        if (it < 1) { "해당 없음" } else { "${it}일" }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
    val myLeave = profileViewModel.myLeave.collectAsState().value!!
    val primary = MaterialTheme.colorScheme.primary
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {
        WheelPickerDialog(
            initIdx = when (openDialog) {
                0 -> {
                    if (myLeave.firstAnnualLeave > 0) {
                        myLeave.firstAnnualLeave - 1
                    } else { 0 }
                }
                1 -> {
                    if (myLeave.secondAnnualLeave > 0) {
                        myLeave.secondAnnualLeave - 1
                    } else { 0 }
                }
                2 -> {
                    if (myLeave.sickLeave > 0) {
                        myLeave.sickLeave - 1
                    } else { 0 }
                }
                else -> { 0 }
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
            fontSize = dpToSp(dp = 20.dp),
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
                text = "1년차 연차",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myLeave.firstAnnualLeave.let {
                        if (it < 0) { "해당 없음" } else { "${it}일" }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                text = "2년차 연차",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myLeave.secondAnnualLeave.let {
                        if (it < 0) { "해당 없음" } else { "${it}일" }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myLeave.sickLeave.let {
                        if (it < 0) { "해당 없음" }
                        else { "${it}일" }
                    },
                    fontSize = dpToSp(dp = 16.dp),
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