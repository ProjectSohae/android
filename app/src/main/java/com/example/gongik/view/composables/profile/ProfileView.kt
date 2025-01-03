package com.example.gongik.view.composables.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gongik.R
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.myinformation.MyInformation
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.dialog.TypingTextDialog
import com.example.gongik.view.composables.dialog.WheelPickerDialog

@Composable
fun ProfileView(
    profileViewModel: ProfileViewModel = viewModel()
) {
    var finishLoadDB by remember {
        mutableStateOf(false)
    }
    var myInformation by remember {
        mutableStateOf<MyInformation?>(null)
    }

    LaunchedEffect(Unit) {

        MyInformationController.initMyInformation { getMyInformation ->
            myInformation = getMyInformation
            finishLoadDB = true
        }
    }

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

            if (myInformation == null) {
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
                        PreviewProfileDetails(myInformation!!)
                    }

                    // 나의 활동
                    item {
                        MyActivities(myInformation!!)
                    }

                    // 복무일
                    item {
                        MilitaryServiceDate(myInformation!!)
                    }

                    // 진급일
                    item {
                        PromotionDate(myInformation!!)
                    }

                    // 복지
                    item {
                        SalaryDetails(myInformation!!)
                    }

                    // 휴가 일수
                    item {
                        RestTimeDetails(myInformation!!)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileViewHeader(

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

                    },
                contentDescription = null
            )
        }
    }
}

// 프로필
@Composable
fun PreviewProfileDetails(
    myInformation: MyInformation
) {
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {

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
            Surface(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(100)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_profile_basic_icon_24),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.offset(y = 4.dp),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.size(12.dp))

            Column {
                Text(
                    text = myInformation.nickname.let {
                        if (it.isBlank()) { "로그인이 필요합니다." }
                        else { myInformation.nickname }
                    },
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = myInformation.emailAddress.let {
                        if (it.isBlank()) { "이메일 없음" }
                        else { myInformation.nickname }
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

// 내가 작성한 글, 댓글
@Composable
fun MyActivities(
    myInformation: MyInformation
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
            fontSize = dpToSp(dp = 20.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
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
        Spacer(modifier = Modifier.size(12.dp))

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

// 복무일
@Composable
fun MilitaryServiceDate(
    myInformation: MyInformation
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
            text = "복무일",
            fontWeight = FontWeight.SemiBold,
            fontSize = dpToSp(dp = 20.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
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
                    text = myInformation.myWorkInformation.startWorkDay.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myWorkInformation.startWorkDay.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
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
                    text = myInformation.myRank.firstPromotionDay.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myWorkInformation.finishWorkDay.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

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
fun PromotionDate(
    myInformation: MyInformation
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
            text = "진급일",
            fontWeight = FontWeight.SemiBold,
            fontSize = dpToSp(dp = 20.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "일병 진급일",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myInformation.myRank.firstPromotionDay.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myRank.firstPromotionDay.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "상병 진급일",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = myInformation.myRank.secondPromotionDay.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myRank.secondPromotionDay.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
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
                    text = myInformation.myRank.thirdPromotionDay.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myRank.thirdPromotionDay.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

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
fun SalaryDetails(
    myInformation: MyInformation
) {
    val primary = MaterialTheme.colorScheme.primary
    val title = listOf(
        "식비",
        "교통비",
    )
    val content = listOf(
        "지원 받는 식비 액수를 입력 하세요.",
        "지원 받는 교통비 액수를 입력 하세요."
    )
    val inputForm = listOf(
        listOf(
            Pair("금액 입력", "원")
        ),
        listOf(
            Pair("금액 입력", "원")
        )
    )
    val initialValuesList = listOf(
        listOf(
            "99,999"
        ),
        listOf(
            "99,999"
        )
    )
    var openDialog by remember { mutableIntStateOf(-1) }

    if (openDialog >= 0) {

        if (openDialog != 2) {
            TypingTextDialog(
                title = title[openDialog],
                content = content[openDialog],
                inputsList = inputForm[openDialog],
                initialValuesList = initialValuesList[openDialog],
                onDismissRequest = { openDialog = -1 },
                onConfirmation = { openDialog = -1 }
            )
        }
        else {
            WheelPickerDialog(
                intensity = 0.95f,
                onDismissRequest = { openDialog = -1 },
                onConfirmation = {},
                optionsList = listOf(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10"
                )
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
            fontSize = dpToSp(dp = 20.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
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
                    text = myInformation.myWelfare.foodCosts.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myWelfare.foodCosts.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
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
                    text = myInformation.myWelfare.transportationCosts.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myWelfare.transportationCosts.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

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
                    text = myInformation.myWorkInformation.startWorkDay.let {
                        if (it < 0) { "해당 없음" }
                        else { myInformation.myWorkInformation.startWorkDay.toString() }
                    },
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

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
fun RestTimeDetails(
    myInformation: MyInformation
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
            text = "휴가 일수",
            fontWeight = FontWeight.SemiBold,
            fontSize = dpToSp(dp = 20.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
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
                    text = "15일",
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
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
                    text = "15일",
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                },
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
                    text = "30일",
                    fontSize = dpToSp(dp = 16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}