package com.example.gongik.view.composables.jobsearch

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gongik.R
import com.example.gongik.view.composables.main.MainNavGraphBarItems
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.dialog.WheelPickerDialog
import com.example.gongik.view.composables.main.MainNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

object JobSearchNavController {
    private var _route = MutableStateFlow("")
    val route = _route.asStateFlow()

    fun navigate(inputRoute: String) {
        _route.value = inputRoute
    }
}

@Composable
fun JobSearchView(
    
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
            JobSearchViewHeader()

            JobSearchViewBody()
        }
    }
}

@Composable
fun JobSearchViewHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "복무지",
            fontSize = dpToSp(dp = 24.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Row {
            // search
            Icon(
                painter = painterResource(id = R.drawable.outline_search_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(1.2f),
                contentDescription = null
            )
        }
    }
}

@Composable
fun JobSearchViewBody(
    jobSearchNavController : NavHostController = rememberNavController()
) {
    val currentSelectedCategory = jobSearchNavController
        .currentBackStackEntryAsState()
        .value?.destination?.route?.let {
            JobSearchCategory.valueOf(it)
        } ?: JobSearchCategory.REVIEW
    var previousSelectedCategory by rememberSaveable {
        mutableStateOf(JobSearchCategory.REVIEW)
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
        JobSearchUpperCategory(currentSelectedCategory)

        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = jobSearchNavController,
            startDestination = JobSearchCategory.REVIEW.name,
            enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
            exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
        ) {
            composable(JobSearchCategory.REVIEW.name) {
                PreviewJobItemsList()
            }
            composable(JobSearchCategory.COMPRETITION.name) {
                JobCompetitionItemsList()
            }
        }
    }
}

// 복무지 리뷰, 이전 경쟁률
@Composable
fun JobSearchUpperCategory(
    currentSelectedCategory : JobSearchCategory
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
        JobSearchCategory.entries.forEach { item ->
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
                            JobSearchNavController.navigate(item.name)
                        }
                    }
            )
        }
    }
}

@Composable
fun PreviewJobItemsList() {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val posts = listOf(
        1,
        2,
        3,
        4,
        5,
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = tertiary,
                            strokeWidth = 4f,
                            start = Offset(0f, this.size.height),
                            end = Offset(this.size.width, this.size.height)
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Text(
                            text = "관할 지방청",
                            fontSize = dpToSp(dp = 16.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable {

                                }
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                    }

                    item {
                        Text(
                            text = "시 • 군 • 구",
                            fontSize = dpToSp(dp = 16.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable {

                                }
                        )
                    }
                }
                
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable {

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 인기순, 리뷰 많은 순 정렬
                    Text(
                        text = "인기순",
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.outline_sort_24),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(
                    items = posts,
                    key = { index: Int, previewPost ->
                        index
                    }
                ) { index: Int, previewPost ->
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(12.dp)
                    ) {
                        PreviewJobItem()
                    }
                }
            }
        }

        // 리뷰 쓰기
        Surface(
            modifier = Modifier
                .size(48.dp)
                .offset(x = (-12).dp, y = (-12).dp)
                .clickable {
                    MainNavController.navigate(MainNavGraphBarItems.WRITEPOST.name)
                },
            shape = RoundedCornerShape(100)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun PreviewJobItem() {
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val tertiary = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawOutline(
                    outline = Outline.Rounded(
                        roundRect = RoundRect(
                            left = 0f,
                            right = this.size.width,
                            top = 0f,
                            bottom = this.size.height,
                            cornerRadius = CornerRadius(10f, 10f)
                        )
                    ),
                    color = tertiary,
                    style = Stroke(width = 2f)
                )
            }
            .clickable {
                MainNavController.navigate(MainNavGraphBarItems.JOBREVIEW.name)
            }
            .padding(16.dp)
    ) {
        Text(
            text = "복무지",
            fontSize = dpToSp(dp = 20.dp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "복무 분야",
            fontSize = dpToSp(dp = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "복무지 주소",
            fontSize = dpToSp(dp = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                tint = Color(0xFFFDCC0D),
                modifier = Modifier.size(16.dp),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "4.0",
                fontSize = dpToSp(dp = 16.dp),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// 이전 경쟁률
@Composable
fun JobCompetitionItemsList() {
    val tertiary = MaterialTheme.colorScheme.tertiary
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var filterNum by rememberSaveable { mutableIntStateOf(0) }
    var year by rememberSaveable { mutableStateOf("") }
    var roundNumber by rememberSaveable { mutableIntStateOf(-1) }
    var intendance by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    val yearList = listOf("2021", "2022", "2023",)
    val roundList = listOf("재학생입영원", "본인 선택")
    val posts = listOf(
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
    )

    if (openDialog) {

        if (filterNum < 2) {
            WheelPickerDialog(
                suffix = filterNum.let { if (it == 0) { "년" } else { "" } },
                intensity = 0.6f,
                onDismissRequest = { openDialog = false },
                onConfirmation = { getSelectedValue ->

                    when (filterNum) {
                        // 접수 년도
                        0 -> { year = getSelectedValue.toString() }
                        // 회차
                        1 -> {
                            roundNumber = getSelectedValue.toString().let {
                                if (it == roundList[0]) { 0 }
                                else { 1 }
                            }
                        }
                    }

                    openDialog = false
                },
                optionsList = filterNum.let { if (it == 0) { yearList } else { roundList } }
            )
        }
        else {
            LaunchedEffect(Unit) {

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = tertiary,
                            strokeWidth = 4f,
                            start = Offset(0f, this.size.height),
                            end = Offset(this.size.width, this.size.height)
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow {
                    item {
                        Text(
                            text = year.ifBlank { "접수 년도" },
                            fontSize = dpToSp(dp = 16.dp),
                            color = year.let {
                                if (it.isBlank()) { MaterialTheme.colorScheme.tertiary }
                                else { MaterialTheme.colorScheme.onPrimary }
                            },
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = year.let {
                                        if (it.isBlank()) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            Color.Transparent
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .background(
                                    color = year.let {
                                        if (it.isBlank()) {
                                            Color.Transparent
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable {
                                    filterNum = 0
                                    openDialog = true
                                }
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                    }

                    // 재학생 입영원, 본인 선택
                    item {
                        Text(
                            text = roundNumber.let {
                                if (it in 0..1) { roundList[it] }
                                else { "회차" }
                            },
                            fontSize = dpToSp(dp = 16.dp),
                            color = roundNumber.let {
                                if (it < 0) { MaterialTheme.colorScheme.tertiary }
                                else { MaterialTheme.colorScheme.onPrimary }
                            },
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = roundNumber.let {
                                        if (it < 0) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            Color.Transparent
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .background(
                                    color = roundNumber.let {
                                        if (it < 0) {
                                            Color.Transparent
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable {
                                    filterNum = 1
                                    openDialog = true
                                }
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                    }

                    item {
                        Text(
                            text = intendance.ifBlank { "관할 지방청" },
                            fontSize = dpToSp(dp = 16.dp),
                            color = intendance.let {
                                if (it.isBlank()) { MaterialTheme.colorScheme.tertiary }
                                else { MaterialTheme.colorScheme.onPrimary }
                            },
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = intendance.let {
                                        if (it.isBlank()) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            Color.Transparent
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .background(
                                    color = intendance.let {
                                        if (it.isBlank()) {
                                            Color.Transparent
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable {
                                    filterNum = 2
                                    openDialog = true
                                }
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                    }

                    item {
                        Text(
                            text = location.ifBlank { "시 • 군 • 구" },
                            fontSize = dpToSp(dp = 16.dp),
                            color = location.let {
                                if (it.isBlank()) { MaterialTheme.colorScheme.tertiary }
                                else { MaterialTheme.colorScheme.onPrimary }
                            },
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = location.let {
                                        if (it.isBlank()) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            Color.Transparent
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .background(
                                    color = location.let {
                                        if (it.isBlank()) {
                                            Color.Transparent
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    },
                                    shape = RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .clickable {
                                    filterNum = 3
                                    openDialog = true
                                }
                        )
                    }
                }
            }

            if (
                year.isBlank()
                || roundNumber < 0
                || intendance.isBlank()
                || location.isBlank()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 72.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "접수 년도, 회차, 관할 지방청, 시 • 군 • 구를 선택해 주세요.",
                        fontSize = dpToSp(dp = 20.dp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            JobDetailsCategory()

                            LazyColumn {
                                item {
                                    Spacer(modifier = Modifier.size(4.dp))
                                }

                                itemsIndexed(
                                    items = posts,
                                    key = { index: Int, previewPost ->
                                        index
                                    }
                                ) { index: Int, previewPost ->
                                    JobCompetitionItem()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobDetailsCategory(

) {
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = primary,
                    strokeWidth = 2f,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height)
                )
            }
            .padding(bottom = 8.dp)
    ) {
        jobDetailsCategory.forEach { item ->
            Text(
                text = item.first,
                fontSize = dpToSp(dp = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(item.second)
            )
        }
    }
}

@Composable
fun JobCompetitionItem(

) {
    val test = listOf(
        "20250120",
        "지방자치단체",
        "울산남구청",
        "3스택+",
        "999:1",
        "999",
        "999",
        "999:1",
        "999",
        "999",
        "999",
        "999",
        "육군훈련소"
    )

    Row(
        modifier = Modifier
            .clickable {

            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        test.forEachIndexed { index: Int, item ->
            Text(
                text = item,
                fontSize = dpToSp(dp = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(jobDetailsCategory[index].second)
            )
        }
    }
}