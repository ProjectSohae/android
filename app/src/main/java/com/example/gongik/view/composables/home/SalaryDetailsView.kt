package com.example.gongik.view.composables.home

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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.gongik.controller.MyInformationController
import com.example.gongik.model.data.SalaryDetails
import com.example.gongik.model.data.myinformation.MyUsedLeave
import com.example.gongik.util.font.dpToSp
import com.example.gongik.util.function.displayAsAmount
import com.example.gongik.view.composables.main.MainNavGraphViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SalaryDetailsView(
    salaryDetails: SalaryDetails,
    onDismissRequest: () -> Unit
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val startDateValue = salaryDetails.beginPayDate.atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val endDateValue = salaryDetails.endPayDate.atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    var leaveList: List<MyUsedLeave> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(Unit) {
        leaveList = MyInformationController.getMyUsedLeaveListByDate(startDateValue, endDateValue)
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(540.dp)
                .shadow(12.dp, RoundedCornerShape(10))
                .clip(RoundedCornerShape(10))
                .hazeChild(
                    state = MainNavGraphViewModel.hazeState,
                    style = HazeStyle(
                        backgroundColor = MaterialTheme.colorScheme.onPrimary,
                        tint = HazeTint(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        blurRadius = 25.dp
                    )
                ) {
                    progressive = HazeProgressive.LinearGradient(
                        startIntensity = 0.9f,
                        endIntensity = 0.9f,
                        preferPerformance = true
                    )
                }
                .padding(vertical = 12.dp)
        ){
            Text(
                text = "급여 명세서",
                fontSize = dpToSp(dp = 20.dp),
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
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
            ) {
                item {
                    SalaryDetailsItem(salaryDetails)
                }
            }

            Text(
                text = "닫기",
                fontSize = dpToSp(dp = 16.dp),
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
    salaryDetails: SalaryDetails
) {
    val totalWorkDay = if (salaryDetails.beginPayDate.monthValue == salaryDetails.endPayDate.monthValue) {
        salaryDetails.totalWorkDay
    } else { salaryDetails.allDayOfStartMonth - salaryDetails.beginPayDate.dayOfMonth + 1 }

    Text(
        text = "${salaryDetails.beginPayDate.year}년 ${salaryDetails.beginPayDate.monthValue}월 기준",
        fontSize = dpToSp(dp = 16.dp),
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth()
    )

    // 보수 등급, 월급
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "보수 등급",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = salaryDetails.startRank,
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "월급",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${displayAsAmount(salaryDetails.startSalaryPerMonth.toString())}원",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // 전체 일수, 일급
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${salaryDetails.beginPayDate.monthValue}월 일수",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${salaryDetails.allDayOfStartMonth}일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "일급",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${displayAsAmount(salaryDetails.startSalary.toString())}원",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // 식비, 교통비
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "식비",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${displayAsAmount(salaryDetails.lunchSupport.toString())}원",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "교통비",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${displayAsAmount(salaryDetails.transportationSupport.toString())}원",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // 근무일, 주말 일수
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "근무일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${totalWorkDay}일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "주말 휴일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${salaryDetails.weekendCount}일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // 휴가 사용일, 누적 복무이탈 일수
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "휴가 사용일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${salaryDetails.weekendCount}일",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "복무이탈",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "없음",
                fontSize = dpToSp(dp = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
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
            fontSize = dpToSp(dp = 12.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${salaryDetails.startSalary} * ${totalWorkDay}\n" +
                    "+ ${salaryDetails.lunchSupport} * (${totalWorkDay} - ${salaryDetails.weekendCount})\n" +
                    "+ ${salaryDetails.transportationSupport} * (${totalWorkDay} - ${salaryDetails.weekendCount})\n" +
                    "= 원",
            fontSize = dpToSp(dp = 12.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(3f)
        )
    }
}