package com.example.gongik.view.composables.home

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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
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
import com.example.gongik.R
import com.example.gongik.controller.BarColorController
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.myinformation.MyInformation
import com.example.gongik.model.data.myinformation.ranksList
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.dialog.TypingTextDialog
import com.example.gongik.view.composables.dialog.WheelPickerDialog

@Composable
fun HomeView(

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
                HomeViewBody(leftPadding, rightPadding)
            }
        }
    }
}

@Composable
fun HomeViewHeader(
    leftPadding : Dp,
    rightPadding : Dp
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    var test by remember {
        mutableStateOf(false)
    }

    if (test){
        TypingTextDialog(){
            test = false
        }
    }

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
                    test = true
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
                .drawBehind {
                    drawCircle(color = onPrimary)
                }
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

@Composable
fun HomeViewBody(
    leftPadding : Dp,
    rightPadding : Dp
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

    if (myInformation == null) {
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
            MyDetails(myInformation!!)

            DateDetails(myInformation!!)
            Spacer(modifier = Modifier.size(12.dp))

            MyVacations(myInformation!!)
            Spacer(modifier = Modifier.size(12.dp))

            UseVacations(myInformation!!)
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun MyDetails(
    myInformation: MyInformation
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = myInformation.nickname.let {
                if (it.isBlank()) { "로그인이 필요합니다." }
                else { myInformation.nickname }
            },
            fontSize = dpToSp(dp = 32.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = myInformation.myWorkInformation.startWorkDay.let {
                if (it < 0) { "직무 없음" }
                else { "사회복무요원" }
            },
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = myInformation.myRank.currentRank.let {
                if (it < 0) { "보수 등급 없음" }
                else { ranksList[myInformation.myRank.currentRank] }
            },
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = myInformation.myWorkInformation.workPlace.let {
                if (it.isBlank()) { "복무지 미정" }
                else { myInformation.myWorkInformation.workPlace }
            },
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DateDetails(
    myInformation: MyInformation
) {
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
                text = myInformation.myWorkInformation.startWorkDay.let {
                    if (it < 0) { "해당 없음" }
                    else { myInformation.myWorkInformation.startWorkDay.toString() }
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
                text = myInformation.myWorkInformation.startWorkDay.let {
                    if (it < 0) { "해당 없음" }
                    else { myInformation.myWorkInformation.startWorkDay.toString() }
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
                text = myInformation.myWorkInformation.startWorkDay.let {
                    if (it < 0) { "해당 없음" }
                    else { myInformation.myWorkInformation.startWorkDay.toString() }
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
                text = myInformation.myRank.currentRank.let {
                    var nextPromotionDay: String = ""

                    if (it == 0) { nextPromotionDay = myInformation.myRank.firstPromotionDay.toString() }
                    else if (it == 1) { nextPromotionDay = myInformation.myRank.secondPromotionDay.toString() }
                    else if (it == 2) { nextPromotionDay = myInformation.myRank.thirdPromotionDay.toString() }

                    if (nextPromotionDay.isBlank()) { "해당 없음" }
                    else { nextPromotionDay }
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
    }
}

@Composable
fun MyVacations(
    myInformation: MyInformation
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

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
                    .scale(1.25f)
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
                Text(
                    text = "2020년 1월",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "200,000",
                        fontSize = dpToSp(dp = 32.dp),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = "원",
                        fontSize = dpToSp(dp = 24.dp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                modifier = Modifier
                    .scale(1.25f)
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
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .background(color = secondary, shape = RoundedCornerShape(30)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
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
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "0회",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            VerticalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp
            )

            Column(
                modifier = Modifier
                    .padding(end = 24.dp)
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
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "0회",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun UseVacations(
    myInformation: MyInformation
) {
    val primary = MaterialTheme.colorScheme.primary
    val useVacationItemsList : List<Pair<String, Int>> = listOf(
        Pair("1년차 연차", R.drawable.outline_annual_leave_24),
        Pair("2년차 연차", R.drawable.outline_annual_leave_24),
        Pair("병가", R.drawable.outline_plus_bottle_24),
        Pair("기타 휴가", R.drawable.outline_annual_leave_24),
        Pair("외출", R.drawable.baseline_leave_early_24),
        Pair("조퇴", R.drawable.baseline_leave_early_24),
        Pair("복무이탈", R.drawable.baseline_warning_amber_24)
    )

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
                .height(560.dp)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            useVacationItemsList.forEach { item ->
                UseVacationsItem(
                    itemName = item.first,
                    iconId = item.second,
                    count = 0
                )
            }
        }
    }
}

@Composable
fun UseVacationsItem(
    itemName : String,
    iconId : Int,
    count : Int
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val secondary = MaterialTheme.colorScheme.secondary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 24.dp)
            .background(color = secondary, shape = RoundedCornerShape(30)),
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
                    modifier = Modifier.drawBehind {
                        drawCircle(
                            color = primaryContainer,
                            radius = this.size.width
                        )
                    },
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(24.dp))

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

                    Text(
                        text = "${count}회 사용",
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dpToSp(dp = 16.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = null
                ) {

                },
                shape = RoundedCornerShape(25)
            ) {
                Text(
                    text = "사용",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}