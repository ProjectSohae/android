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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.gongik.R
import com.example.gongik.util.font.dpToSp

@Composable
fun ProfileView(

) {
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
            ProfileViewHeader()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    PreviewProfileDetails()
                }

                item {
                    MyActivities()
                }

                item {
                    MilitaryServiceDate()
                }

                item {
                    PromotionDate()
                }

                item {
                    SalaryDetails()
                }

                item {
                    RestTimeDetails()
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
            .padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
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
                    text = "닉네임",
                    fontSize = dpToSp(dp = 16.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "이메일",
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
                    text = "2024년 1월 1일",
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
                    text = "2024년 1월 1일",
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
                    text = "2024년 1월 1일",
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
                    text = "2024년 1월 1일",
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
                    text = "2024년 1월 1일",
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

// 급여
@Composable
fun SalaryDetails(

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
            text = "급여",
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
                text = "식비",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "7000원",
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
                text = "교통비",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "7000원",
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
                text = "월급 계산 시작일",
                fontSize = dpToSp(dp = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1일",
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