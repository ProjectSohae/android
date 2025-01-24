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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gongik.R
import com.example.gongik.view.composables.main.MainNavGraphItems
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.dialog.WheelPickerDialog
import com.example.gongik.view.composables.main.MainNavGraphViewModel
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
    jobSearchViewModel: JobSearchViewModel = viewModel()
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

            JobSearchViewBody(jobSearchViewModel)
        }
    }
}

@Composable
private fun JobSearchViewHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "복무 정보",
            fontSize = dpToSp(dp = 24.dp),
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
                        MainNavGraphViewModel.navigate(MainNavGraphItems.SEARCHJOB.name)
                    },
                contentDescription = null
            )
        }
    }
}

@Composable
private fun JobSearchViewBody(
    jobSearchViewModel: JobSearchViewModel,
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
                PreviewJobListView(jobSearchViewModel)
            }
            composable(JobSearchCategory.COMPRETITION.name) {
                JobCompetitionListView(jobSearchViewModel)
            }
        }
    }
}

// 복무지 리뷰, 이전 경쟁률
@Composable
private fun JobSearchUpperCategory(
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
                fontSize = dpToSp(dp = 16.dp),
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

// 복무지 목록
@Composable
private fun PreviewJobListView(
    jobSearchViewModel: JobSearchViewModel
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val previewJobList = listOf(1, 2, 3, 4, 5,)
    var ghjbc_cd by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var pressedFilterIdx by rememberSaveable { mutableIntStateOf(-1) }
    var sortBy by rememberSaveable { mutableStateOf(jobSearchViewModel.sortByList[0]) }
    val filterOptionsList = listOf(
        jobSearchViewModel.ghjbc_cd.keys.toList(),
        emptyList(),
        jobSearchViewModel.sortByList
    )
    val getItemIdx: (String, List<String>) -> Int = { value, optionsList ->
        var tmp = 0

        optionsList.forEachIndexed breaker@{ index, optionValue ->
            if (optionValue == value) {
                tmp = index
                return@breaker
            }
        }

        tmp
    }

    if (pressedFilterIdx >= 0) {

        // 관할 지방청, 별점순 정렬
        if (pressedFilterIdx == 0 || pressedFilterIdx == 2) {
            WheelPickerDialog(
                initIdx = when (pressedFilterIdx) {
                    0 -> { getItemIdx(ghjbc_cd, filterOptionsList[pressedFilterIdx]) }
                    2 -> { getItemIdx(sortBy, filterOptionsList[pressedFilterIdx]) }
                    else -> { 0 }
                },
                intensity = 0.8f,
                onDismissRequest = { pressedFilterIdx = -1 },
                onConfirmation = { getOptionValue ->

                    when (pressedFilterIdx) {
                        0 -> { ghjbc_cd = getOptionValue.toString() }
                        2 -> { sortBy = getOptionValue.toString() }
                    }

                    pressedFilterIdx = -1
                },
                optionsList = filterOptionsList[pressedFilterIdx]
            )
        }
        // 시.군.구
        else {
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 복무지 검색 필터
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = tertiary,
                            strokeWidth = 1.dp.toPx(),
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
                        JobSearchFilterItem(
                            filterName = "관할 지방청",
                            filterValue = ghjbc_cd,
                            pressFilterItem = { pressedFilterIdx = 0 }
                        )
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
                                .clickable { pressedFilterIdx = 1 }
                        )
                    }
                }
                
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable { pressedFilterIdx = 2 },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 인기순, 리뷰 많은 순 정렬
                    Text(
                        text = sortBy,
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

            // 복무지 목록
            if (ghjbc_cd.isBlank() || address.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "관할 지방청, 시 • 군 • 구를\n선택해 주세요.",
                        textAlign = TextAlign.Center,
                        fontSize = dpToSp(dp = 20.dp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onPrimary),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(
                        items = previewJobList,
                        key = { index: Int, previewPost ->
                            index
                        }
                    ) { index: Int, previewPost ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            PreviewJobItem()

                            // 광고
                            if (index % 2 == 0) {
                                Surface(
                                    modifier = Modifier
                                        .padding(top = 24.dp)
                                        .fillParentMaxWidth()
                                        .height(120.dp)
                                ) {

                                }
                            }
                        }
                    }
                }
            }
        }

        // 리뷰 쓰기
        Surface(
            modifier = Modifier
                .size(48.dp)
                .offset(x = (-12).dp, y = (-12).dp)
                .clip(RoundedCornerShape(100))
                .clickable {
                    MainNavGraphViewModel.navigate(MainNavGraphItems.WRITEJOBREVIEW.name)
                }
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
private fun JobSearchFilterItem(
    modifier: Modifier = Modifier,
    filterName: String,
    filterValue: String,
    pressFilterItem: () -> Unit
) {
    Text(
        text = filterValue.ifBlank { filterName },
        fontSize = dpToSp(dp = 16.dp),
        color = filterValue.let {
            if (it.isBlank()) {
                MaterialTheme.colorScheme.tertiary
            } else { MaterialTheme.colorScheme.onPrimary }
        },
        modifier = modifier
            .padding(end = 12.dp)
            .border(
                width = 1.dp,
                color = filterValue.let {
                    if (it.isBlank()) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Color.Transparent
                    }
                },
                shape = RoundedCornerShape(100)
            )
            .background(
                color = filterValue.let {
                    if (it.isBlank()) {
                        Color.Transparent
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                },
                shape = RoundedCornerShape(100)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { pressFilterItem() }
    )
}

@Composable
private fun PreviewJobItem() {
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
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            .clickable {
                MainNavGraphViewModel.navigate(MainNavGraphItems.JOBREVIEW.name)
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
private fun JobCompetitionListView(
    jobSearchViewModel: JobSearchViewModel
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var filterNum by rememberSaveable { mutableIntStateOf(0) }
    var jeopsu_yy by rememberSaveable { mutableStateOf("") }
    var jeopsu_tms by rememberSaveable { mutableStateOf("") }
    var ghjbc_cd by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    val filterOptionsList = listOf(
        jobSearchViewModel.jeopsu_yy.keys.toList(),
        jobSearchViewModel.jeopsu_tms.keys.toList(),
        jobSearchViewModel.ghjbc_cd.keys.toList()
    )
    val getItemIdx: (String) -> Int = {
        var tmp = 0

        filterOptionsList[filterNum].forEachIndexed { idx, item ->
            if (it == item) { tmp = idx }
        }

        tmp
    }
    val posts = listOf(
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
    )

    if (openDialog) {

        if (filterNum < 3) {
            WheelPickerDialog(
                initIdx = when (filterNum) {
                    0 -> { getItemIdx(jeopsu_yy) }
                    1 -> { getItemIdx(jeopsu_tms) }
                    2 -> { getItemIdx(ghjbc_cd) }
                    else -> { 0 }
                },
                intensity = 0.85f,
                onDismissRequest = { openDialog = false },
                onConfirmation = { getSelectedValue ->

                    when (filterNum) {
                        // 접수 년도
                        0 -> { jeopsu_yy = getSelectedValue.toString() }
                        // 회차
                        1 -> { jeopsu_tms = getSelectedValue.toString() }
                        // 관할 지방청
                        2 -> { ghjbc_cd = getSelectedValue.toString() }
                    }

                    openDialog = false
                },
                optionsList = filterOptionsList[filterNum]
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
            // 접수 년도, 회차, 관할 지방청, 시.군.구 필터
            LazyRow(
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
                    .padding(vertical = 12.dp)
            ) {
                // 접수 년도
                item {
                    JobSearchFilterItem(
                        modifier = Modifier.padding(start = 16.dp),
                        filterName = "접수 년도",
                        filterValue = jeopsu_yy,
                        pressFilterItem = {
                            filterNum = 0
                            openDialog = true
                        }
                    )
                }

                // 재학생 입영원, 본인 선택
                item {
                    JobSearchFilterItem(
                        filterName = "회차",
                        filterValue = jeopsu_tms,
                        pressFilterItem = {
                            filterNum = 1
                            openDialog = true
                        }
                    )
                }

                // 관할 지방청
                item {
                    JobSearchFilterItem(
                        filterName = "관할 지방청",
                        filterValue = ghjbc_cd,
                        pressFilterItem = {
                            filterNum = 2
                            openDialog = true
                        }
                    )
                }

                // 시.군.구
                item {
                    JobSearchFilterItem(
                        filterName = "시 • 군 • 구",
                        filterValue = "",
                        pressFilterItem = {

                        }
                    )
                }
            }

            var pressedItemIdx by rememberSaveable { mutableIntStateOf(-1) }

            if (
                false
//                yearIdx < 0
//                || roundNumberIdx < 0
//                || intendance.isBlank()
//                || address.isBlank()
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
                                itemsIndexed(
                                    items = posts,
                                    key = { index: Int, item ->
                                        index
                                    }
                                ) { index: Int, item ->
                                    JobCompetitionItem(
                                        drawOverline = (index > 0),
                                        isPressed = (index == pressedItemIdx),
                                        pressItem = {
                                            if (index == pressedItemIdx) {
                                                pressedItemIdx = -1
                                            } else { pressedItemIdx = index }
                                        }
                                    )
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
private fun JobDetailsCategory(

) {
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = primary,
                    strokeWidth = 1.dp.toPx(),
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
                color = primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(item.second)
            )
        }
    }
}

@Composable
private fun JobCompetitionItem(
    drawOverline: Boolean,
    isPressed: Boolean,
    pressItem: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
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
            .background(
                if (isPressed) {
                    primary
                } else {
                    onPrimary
                }
            )
            .clickable { pressItem() }
            .drawBehind {
                if (drawOverline) {
                    drawLine(
                        color = primary,
                        start = Offset(0f, 0f),
                        end = Offset(this.size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        test.forEachIndexed { index: Int, item ->
            Text(
                text = item,
                fontSize = dpToSp(dp = 16.dp),
                color = if (isPressed) { onPrimary } else { primary },
                textAlign = TextAlign.Center,
                modifier = Modifier.width(jobDetailsCategory[index].second)
            )
        }
    }
}