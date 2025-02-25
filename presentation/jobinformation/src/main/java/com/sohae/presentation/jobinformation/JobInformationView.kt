package com.sohae.presentation.jobinformation

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.sohae.common.resource.R
import com.sohae.controller.mainnavgraph.MainNavController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.domain.jobreview.entity.JobReviewScoreNamesList
import kotlinx.coroutines.runBlocking

@Composable
fun JobInformationView() {
    NaverMapSdk.getInstance(LocalContext.current).client =
        NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_CLIENT)

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
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            JobInformationViewHeader()

            JobInformationBody()
        }
    }
}

@Composable
private fun JobInformationViewHeader() {
    val tertiary = MaterialTheme.colorScheme.tertiary

    Box(
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "복무지 정보",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    MainNavController.popBack()
                }
        )
    }
}

@Composable
private fun JobInformationBody(
    jobReviewNavController : NavHostController = rememberNavController()
) {
    val currentSelectedCategory = jobReviewNavController
        .currentBackStackEntryAsState()
        .value?.destination?.route?.let {
            JobInformationCategory.valueOf(it)
        } ?: JobInformationCategory.PROFILE
    var previousSelectedCategory by rememberSaveable {
        mutableStateOf(JobInformationCategory.PROFILE)
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
        JobSimpleInformation()

        JobInformationCategory(currentSelectedCategory)

        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = jobReviewNavController,
            startDestination = JobInformationCategory.PROFILE.name,
            enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
            exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
        ) {
            composable(JobInformationCategory.PROFILE.name) {
                JobDetailsView()
            }
            composable(JobInformationCategory.REVIEW.name) {
                JobReviewItemsList()
            }
        }
    }
}

@Composable
private fun JobSimpleInformation() {
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
                fontSize = 24.sp,
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
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Text(
                    text = "(999개 리뷰)",
                    fontSize = 12.sp,
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
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable {
                    MainNavController.navigate(MainNavGraphRoutes.WRITEJOBREVIEW.name)
                }
                .padding(horizontal = 24.dp, vertical = 4.dp)
        )
    }
}

// 복무지 소개, 리뷰
@Composable
private fun JobInformationCategory(
    currentSelectedCategory: JobInformationCategory
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
        JobInformationCategory.entries.forEach { item ->
            Text(
                text = item.categoryName,
                fontSize = 16.sp,
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

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun JobDetailsView(
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
                    fontSize = 20.sp,
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
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textAlign = TextAlign.Start,
                                    )

                                    Text(
                                        text = "사회복지시설",
                                        fontSize = 12.sp,
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
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "서울",
                                        fontSize = 12.sp,
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
            val coordinate = LatLng(37.5666103, 126.9783882)

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "지도",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                NaverMap(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 24.dp)
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(5)),
                    cameraPositionState = CameraPositionState(
                        position = CameraPosition(
                            coordinate, 16.0
                        )
                    )
                ) {
                    Marker(
                        state = MarkerState(coordinate),
                        captionText = "복무지"
                    )
                }
            }
        }

        // 광고
        item {
            Surface(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.primary
            ) {}
        }

        // 복무지 재학생입영원 이력
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "전년도 재학생입영원 이력",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp)
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
                                fontSize = 12.sp,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "전년도 본인선택 이력",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp)
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
                                fontSize = 12.sp,
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
                JobReviewItemView()

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
private fun JobReviewItemView() {
    var isFolded by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .clickable {
                MainNavController.setParam("pressed_job_review_id", 0)
                MainNavController.navigate(MainNavGraphRoutes.JOBREVIEW.name)
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
                fontSize = 20.sp,
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
                        fontSize = 16.sp,
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
                        fontSize = 12.sp,
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
                        fontSize = 24.sp,
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
                                fontSize = 12.sp,
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
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 주요 업무
        Text(
            text = "주요 업무",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "주요 업무",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 장점
        Text(
            text = "장점",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "장점",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(12.dp))

        // 단점
        Text(
            text = "단점",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "단점",
            fontSize = 16.sp,
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
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}