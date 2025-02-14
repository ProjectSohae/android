package com.jhw.sohae.presentation.jobnavgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jhw.sohae.controller.mainnavgraph.MainNavController
import com.jhw.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.jhw.sohae.presentation.jobapplyhistory.JobApplyHistoryView
import com.jhw.sohae.presentation.jobinfolist.PreviewJobInfoListView
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobInfoNavGraphView(
    jobInfoNavGraphViewModel: JobInfoNavGraphViewModel = viewModel()
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
            modifier = Modifier.padding(innerPadding)
        ) {
            JobInfoNavGraphViewHeader()

            JobInfoNavGraphViewBody(jobInfoNavGraphViewModel)
        }
    }
}

@Composable
private fun JobInfoNavGraphViewHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "복무 정보",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Row {
            // search
            Icon(
                painter = painterResource(id = R.drawable.outline_search_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        MainNavController.navigate(MainNavGraphRoutes.SEARCHJOB.name)
                    },
                contentDescription = null
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun JobInfoNavGraphViewBody(
    jobInfoNavGraphViewModel: JobInfoNavGraphViewModel,
    jobSearchNavController : NavHostController = rememberNavController()
) {
    val currentSelectedCategory = jobSearchNavController
        .currentBackStackEntryAsState()
        .value?.destination?.route?.let {
            JobSearchCategory.valueOf(it)
        } ?: JobSearchCategory.INFORMATION
    var previousSelectedCategory by rememberSaveable {
        mutableStateOf(JobSearchCategory.INFORMATION)
    }
    val currentRoute = JobSearchNavController.route.collectAsState().value
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
            jobSearchNavController.navigate(currentRoute) {
                jobSearchNavController.popBackStack()
            }
            JobSearchNavController.navigate("")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        JobInfoNavGraphCategory(currentSelectedCategory)

        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = jobSearchNavController,
            startDestination = JobSearchCategory.INFORMATION.name,
            enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
            exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
        ) {
            composable(JobSearchCategory.INFORMATION.name) {
                PreviewJobInfoListView()
            }
            composable(JobSearchCategory.COMPRETITION.name) {
                JobApplyHistoryView()
            }
        }
    }
}

// 복무지 리뷰, 이전 경쟁률
@Composable
private fun JobInfoNavGraphCategory(
    currentSelectedCategory : JobSearchCategory
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    var selectedCategory by rememberSaveable { mutableIntStateOf(currentSelectedCategory.idx) }
    val underLineStartPos = animateFloatAsState(targetValue = selectedCategory.toFloat()).value

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
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        (this.size.width / 2f) * (0.1f + underLineStartPos),
                        this.size.height
                    ),
                    end = Offset(
                        (this.size.width / 2f) * (0.9f + underLineStartPos),
                        this.size.height
                    ),
                    strokeWidth = 2.dp.toPx()
                )
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        JobSearchCategory.entries.forEach { item ->
            Text(
                text = item.categoryName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = selectedCategory.let {
                    if (it == item.idx) { primary } else { tertiary }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (selectedCategory != item.idx) {
                            selectedCategory = item.idx
                            JobSearchNavController.navigate(item.name)
                        }
                    }
            )
        }
    }
}