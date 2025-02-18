package com.jhw.sohae.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.sp
import com.jhw.sohae.controller.mainnavgraph.MainScreenController
import com.jhw.sohae.domain.myinformation.entity.MyUsedLeaveEntity
import com.jhw.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.jhw.sohae.home.entity.SalaryDetailsEntity
import com.jhw.utils.displayAsAmount
import com.jhw.utils.getWeekendCount
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.hazeEffect
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalaryDetailsView(
    salaryDetails: SalaryDetailsEntity,
    homeViewModel: HomeViewModel,
    onDismissRequest: () -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val startDateValue = salaryDetails.beginPayDate.atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val endDateValue = salaryDetails.endPayDate.atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    var leaveList: List<MyUsedLeaveEntity> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(Unit) {
        leaveList = homeViewModel.getMyUsedLeaveListByDate(startDateValue, endDateValue)
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
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
                .padding(vertical = 12.dp)
        ){
            Text(
                text = "급여 명세서",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
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
                    .padding(bottom = 12.dp),
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                item {
                    SalaryDetailsItem(
                        payDate = salaryDetails.beginPayDate,
                        lastPayDate =
                        if (salaryDetails.beginPayDate.monthValue == salaryDetails.endPayDate.monthValue) {
                            salaryDetails.endPayDate
                        } else {
                            LocalDate.of(salaryDetails.beginPayDate.year, salaryDetails.beginPayDate.monthValue, salaryDetails.allDayOfStartMonth)
                        },
                        rank = salaryDetails.startRank,
                        salaryValue = salaryDetails.startSalaryPerMonth,
                        slackOffCount = salaryDetails.slackOffCountInStartMonth,
                        lunchSupport = salaryDetails.lunchSupport,
                        noLunchSupportCount = salaryDetails.noLunchSupportCountInStartMonth,
                        transportationSupport = salaryDetails.transportationSupport,
                        noTransportationSupportCount = salaryDetails.noTransportationSupportCountInStartMonth,
                        allDayOfMonth = salaryDetails.allDayOfStartMonth,
                        totalWorkDay =
                        if (salaryDetails.beginPayDate.monthValue == salaryDetails.endPayDate.monthValue) {
                            salaryDetails.totalWorkDay
                        } else {
                            salaryDetails.allDayOfStartMonth - salaryDetails.beginPayDate.dayOfMonth + 1
                        }
                    )
                }

                item {
                    if (salaryDetails.beginPayDate.monthValue != salaryDetails.endPayDate.monthValue) {
                        SalaryDetailsItem(
                            payDate = LocalDate.of(salaryDetails.endPayDate.year, salaryDetails.endPayDate.monthValue, 1),
                            lastPayDate = salaryDetails.endPayDate,
                            rank = salaryDetails.endRank,
                            salaryValue = salaryDetails.endSalaryPerMonth,
                            slackOffCount = salaryDetails.slackOffCountInEndMonth,
                            lunchSupport = salaryDetails.lunchSupport,
                            noLunchSupportCount = salaryDetails.noLunchSupportCountInEndMonth,
                            transportationSupport = salaryDetails.transportationSupport,
                            noTransportationSupportCount = salaryDetails.noTransportationSupportCountInEndMonth,
                            allDayOfMonth = salaryDetails.allDayOfEndMonth,
                            totalWorkDay = salaryDetails.endPayDate.dayOfMonth
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "총 급여 합산액",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "${displayAsAmount(salaryDetails.resultSalary.toString())}원",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                    }
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
                    .padding(top = 12.dp)
                    .clickable { onDismissRequest() }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalaryDetailsItem(
    payDate: LocalDate,
    lastPayDate: LocalDate,
    rank: String,
    salaryValue: Int,
    slackOffCount: Int,
    lunchSupport: Int,
    noLunchSupportCount: Int,
    transportationSupport: Int,
    noTransportationSupportCount: Int,
    allDayOfMonth: Int,
    totalWorkDay: Int,
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val weekendCount = getWeekendCount(payDate, lastPayDate)

    Column(
        modifier = Modifier
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
        Text(
            text = "${payDate.year}년 ${payDate.monthValue}월 기준",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .drawBehind {
                    drawLine(
                        color = tertiary,
                        start = Offset(this.size.width / 2f, 0f),
                        end = Offset(this.size.width / 2f, this.size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            // 보수 등급, 월급
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "보수 등급",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = rank,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "월급",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${displayAsAmount(salaryValue.toString())}원",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 전체 일수, 일급
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${payDate.monthValue}월 일수",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${allDayOfMonth}일",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "일급",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${displayAsAmount((salaryValue / allDayOfMonth).toString())}원",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 식비, 교통비
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "식비",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${displayAsAmount(lunchSupport.toString())}원",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "교통비",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${displayAsAmount(transportationSupport.toString())}원",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 근무일, 주말 일수
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "근무일",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = totalWorkDay.let {
                            if (it < 1) { "없음" } else { "${it}일" }
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "주말 휴일",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = weekendCount.let {
                            if (it < 1) { "없음" } else { "${it}일" }
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 식비 미지급 일수, 교통비 미지급 일수
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "식비 미지급 휴가",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = noLunchSupportCount.let {
                            if (it < 1) { "없음" } else { "${it}일" }
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "교통비 미지급 휴가",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = noTransportationSupportCount.let {
                            if (it < 1) { "없음" } else { "${it}일" }
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 복무 이탈
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
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "복무이탈",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = slackOffCount.let {
                            if (it < 1) { "없음" } else { "${it}일" }
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {}
            }
        }

        // 총 급여 계산
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "급여 총액",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${displayAsAmount((salaryValue / allDayOfMonth).toString())} * (${totalWorkDay} - ${slackOffCount})\n" +
                        "+ ${displayAsAmount(lunchSupport.toString())} * (${totalWorkDay} - (${weekendCount} + ${noLunchSupportCount}))\n" +
                        "+ ${displayAsAmount(transportationSupport.toString())} * (${totalWorkDay} - (${weekendCount} + ${noTransportationSupportCount}))\n" +
                        "= ${
                            displayAsAmount(
                                (
                                        ((salaryValue / allDayOfMonth) * (totalWorkDay - slackOffCount))
                                                + (lunchSupport * (totalWorkDay - (weekendCount + noLunchSupportCount)))
                                                + (transportationSupport * (totalWorkDay - (weekendCount + noTransportationSupportCount)))
                                        ).toString()
                            )
                        }원",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(3f)
            )
        }
    }
}