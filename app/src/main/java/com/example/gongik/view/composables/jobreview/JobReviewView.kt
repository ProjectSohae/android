package com.example.gongik.view.composables.jobreview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gongik.R
import com.example.gongik.view.composables.jobsearch.JobReviewScoreNamesList
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavGraphItems
import com.example.gongik.view.composables.main.MainNavGraphViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

object JobReviewNavController{
    private var _route = MutableStateFlow("")
    val route = _route.asStateFlow()

    fun navigate(inputRoute: String) {
        _route.value = inputRoute
    }
}

@Composable
fun JobReviewView() {
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
            modifier = Modifier.padding(innerPadding)
        ) {
            JobReviewViewHeader()

            JobReviewBody()
        }
    }
}

@Composable
private fun JobReviewViewHeader() {
    val tertiary = MaterialTheme.colorScheme.tertiary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = tertiary,
                    strokeWidth = 2f,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height)
                )
            }
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "복무지 정보",
            fontSize = dpToSp(dp = 24.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun JobReviewBody(
    jobReviewNavController : NavHostController = rememberNavController()
) {
    val currentSelectedCategory = jobReviewNavController
        .currentBackStackEntryAsState()
        .value?.destination?.route?.let {
            JobReviewCategory.valueOf(it)
        } ?: JobReviewCategory.INFORMATION
    var previousSelectedCategory by rememberSaveable {
        mutableStateOf(JobReviewCategory.INFORMATION)
    }
    val currentRoute = JobReviewNavController.route.collectAsState().value
    var transitionDir = 1

    LaunchedEffect(currentSelectedCategory) {
        runBlocking {

            (currentSelectedCategory.idx - previousSelectedCategory.idx).let {
                transitionDir = if (it > 0) { 1 } else { -1 }
            }

            previousSelectedCategory = currentSelectedCategory
        }
    }

    LaunchedEffect(currentRoute) {

        if (currentRoute.isNotBlank()) {
            jobReviewNavController.navigate(currentRoute) {
                jobReviewNavController.popBackStack()
            }
            JobReviewNavController.navigate("")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        JobReviewSimpleInformation()

        JobReviewCategory(currentSelectedCategory)

        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = jobReviewNavController,
            startDestination = JobReviewCategory.INFORMATION.name,
            enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
            exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
        ) {
            composable(JobReviewCategory.INFORMATION.name) {
                JobInformation()
            }
            composable(JobReviewCategory.REVIEW.name) {
                JobReviewItemsList()
            }
        }
    }
}

@Composable
private fun JobReviewSimpleInformation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "서울교통공사",
                fontWeight = FontWeight.SemiBold,
                fontSize = dpToSp(dp = 24.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    tint = Color(0xFFFDCC0D),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
                Text(
                    text = " 4.0 ",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dpToSp(dp = 16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Text(
                    text = "(999개 리뷰)",
                    fontSize = dpToSp(dp = 12.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }

        Text(
            text = "리뷰하기",
            fontWeight = FontWeight.Medium,
            fontSize = dpToSp(dp = 12.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable {
                    MainNavGraphViewModel.navigate(MainNavGraphItems.WRITEJOBREVIEW.name)
                }
                .padding(horizontal = 24.dp, vertical = 4.dp)
        )
    }
}

// 복무지 소개, 리뷰
@Composable
private fun JobReviewCategory(
    currentSelectedCategory: JobReviewCategory
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    var selectedCategory by rememberSaveable { mutableIntStateOf(currentSelectedCategory.idx) }
    val lowerLineStartPos = animateFloatAsState(targetValue = selectedCategory.toFloat()).value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = tertiary,
                    start = Offset(this.size.width / 2f, this.size.height * 0.35f),
                    end = Offset(this.size.width / 2f, this.size.height * 0.75f),
                    strokeWidth = 4f
                )
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1f
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        (this.size.width / 2f) * (0.1f + lowerLineStartPos),
                        this.size.height - 2f
                    ),
                    end = Offset(
                        (this.size.width / 2f) * (0.9f + lowerLineStartPos),
                        this.size.height - 2f
                    ),
                    strokeWidth = 4f
                )
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        JobReviewCategory.entries.forEach { item ->
            Text(
                text = item.categoryName,
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.SemiBold,
                color = selectedCategory.let {
                    if (it == item.idx) { primary }
                    else { tertiary }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (selectedCategory != item.idx) {
                            selectedCategory = item.idx
                            JobReviewNavController.navigate(item.name)
                        }
                    }
            )
        }
    }
}

@Composable
private fun JobInformation(
    jobInformationViewModel: JobInformationViewModel = viewModel()
) {
    val jobInformationDetailsCount = jobInfotmationDetails.size
    val tertiary = MaterialTheme.colorScheme.tertiary

    LaunchedEffect(Unit) {

    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // 복무지 소개
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "서울교통공사 복무지 소개",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp)
                ) {
                    for (index: Int in 0..jobInformationDetailsCount / 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (index * 2 < jobInformationDetailsCount) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 12.dp),
                                ) {
                                    Text(
                                        text = jobInfotmationDetails[index * 2],
                                        fontSize = dpToSp(dp = 12.dp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textAlign = TextAlign.Start,
                                    )

                                    Text(
                                        text = "사회복지시설",
                                        fontSize = dpToSp(dp = 12.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            if (index * 2 + 1 < jobInformationDetailsCount) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp),
                                ) {
                                    Text(
                                        text = jobInfotmationDetails[index * 2 + 1],
                                        fontSize = dpToSp(dp = 12.dp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "서울",
                                        fontSize = dpToSp(dp = 12.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 복무지 위치
        item {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "지도",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        shape = RoundedCornerShape(5)
                    ) {

                    }
                }
            }
        }

        // 광고
        item {
            Surface(
                modifier = Modifier.fillParentMaxWidth().height(120.dp).padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.primary
            ) {}
        }

        // 복무지 재학생입영원 이력
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "재학생입영원 이력",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(0f, this.size.height),
                                    end = Offset(this.size.width, this.size.height)
                                )
                            }
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        jobCompetitionRecordCategory.forEachIndexed { index, item ->
                            Text(
                                text = item,
                                fontSize = dpToSp(dp = 12.dp),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.tertiary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 복무지 본인선택 이력
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "본인선택 이력",
                    fontSize = dpToSp(dp = 20.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .drawBehind {
                                drawLine(
                                    color = tertiary,
                                    start = Offset(0f, this.size.height),
                                    end = Offset(this.size.width, this.size.height)
                                )
                            }
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        jobCompetitionRecordCategory.forEachIndexed { index, item ->
                            Text(
                                text = item,
                                fontSize = dpToSp(dp = 12.dp),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.tertiary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JobReviewItemsList() {
    val posts = listOf(
        1,1,1,1,1,1,1,
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(
                    ColorUtils.blendARGB(
                        MaterialTheme.colorScheme.tertiary.toArgb(),
                        MaterialTheme.colorScheme.onPrimary.toArgb(),
                        0.85f
                    )
                )
            ),
    ) {
        itemsIndexed(
            items = posts,
            key = { index: Int, previewPost ->
                index
            }
        ) { index: Int, previewPost ->
            Column {
                JobReviewItem()

                // 광고
                Box(
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    if (index % 2 == 0) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JobReviewItem() {
    var isFolded by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .clickable {

            }
            .padding(16.dp)
    ) {
        // 복무지
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "복무지",
                fontSize = dpToSp(dp = 20.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            if (isFolded) {
                Row(
                    modifier = Modifier.clickable { isFolded = false },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        tint = Color(0xFFFDCC0D),
                        modifier = Modifier.size(16.dp),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = "4.0",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                        contentDescription = null
                    )
                }
            }
            else {
                Row(
                    modifier = Modifier
                        .clickable { isFolded = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "접기",
                        fontWeight = FontWeight.Medium,
                        fontSize = dpToSp(dp = 12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                        contentDescription = null
                    )
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (!isFolded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_star_24),
                        tint = Color(0xFFFDCC0D),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "4.0",
                        fontSize = dpToSp(dp = 24.dp),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    JobReviewScoreNamesList.forEach { scoreName ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = scoreName,
                                fontSize = dpToSp(dp = 12.dp),
                                color = MaterialTheme.colorScheme.primary
                            )

                            Row {
                                for (idx: Int in 1..5) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_star_24),
                                        tint = if (idx < 4) {
                                            Color(0xFFFDCC0D)
                                        } else { Color.Gray },
                                        modifier = Modifier.size(16.dp),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
        }

        // 제목
        Text(
            text = "“도망쳐도망쳐도망쳐도망쳐도망쳐도망쳐도망쳐”",
            fontSize = dpToSp(dp = 20.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 주요 업무
        Text(
            text = "주요 업무",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "주요 업무",
            fontSize = dpToSp(dp = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 장점
        Text(
            text = "장점",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "장점",
            fontSize = dpToSp(dp = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 단점
        Text(
            text = "단점",
            fontSize = dpToSp(dp = 16.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "단점",
            fontSize = dpToSp(dp = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 추천
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(20)
                )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_thumb_up_off_alt_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "도움이 돼요 (0)",
                    fontSize = dpToSp(dp = 12.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}