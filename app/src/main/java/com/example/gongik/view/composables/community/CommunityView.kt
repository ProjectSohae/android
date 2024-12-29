package com.example.gongik.view.composables.community

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gongik.R
import com.example.gongik.model.viewmodel.BarColorController
import com.example.gongik.model.viewmodel.CommunityCategories
import com.example.gongik.model.viewmodel.CommunityViewModel
import com.example.gongik.model.viewmodel.MainNavGraphBarItems
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.main.MainNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

object CommunityNavController{
    private var _route = MutableStateFlow("")
    val route = _route.asStateFlow()

    fun navigate(inputRoute: String) {
        _route.value = inputRoute
    }
}

@Composable
fun CommunityView(
    communityViewModel: CommunityViewModel
) {
    BarColorController.setNavigationBarColor(MaterialTheme.colorScheme.onPrimary)

    Scaffold {
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
            CommunityViewHeader()

            CommunityViewBody(savedStartDestination = CommunityCategories.ALL)
        }
    }
}

@Composable
fun CommunityViewHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "커뮤니티",
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
            Spacer(modifier = Modifier.size(16.dp))

            // notification
            Icon(
                painter = painterResource(id = R.drawable.outline_notifications_none_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(1.2f),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(16.dp))

            // my posts list
            Icon(
                painter = painterResource(id = R.drawable.profile_24),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(1.2f),
                contentDescription = null
            )
        }
    }
}

@Composable
fun CommunityViewBody(
    savedStartDestination : CommunityCategories,
    communityNavController : NavHostController = rememberNavController()
) {
    val currentSelectedCategory = communityNavController
        .currentBackStackEntryAsState()
        .value?.destination?.route?.let {
            CommunityCategories.valueOf(it)
        } ?: savedStartDestination
    var previousSelectedCategory by rememberSaveable {
        mutableStateOf(savedStartDestination)
    }
    var savedSelectedSubCategory by rememberSaveable {
        mutableIntStateOf(-1)
    }
    val currentRoute = CommunityNavController.route.collectAsState().value
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
            communityNavController.navigate(currentRoute) {
                communityNavController.popBackStack()
            }

            if (currentRoute == CommunityCategories.HOT.name) {
                savedSelectedSubCategory = 0
            }
            else {
                savedSelectedSubCategory = -1
            }

            CommunityNavController.navigate("")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommunityUpperCategory(currentSelectedCategory)

        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = communityNavController,
                startDestination = savedStartDestination.name,
                enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
                exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
            ) {
                composable(CommunityCategories.ALL.name) {
                    CommunityPostsListBody(
                        savedSelectedSubCategory,
                        CommunityCategories.ALL
                    )
                }
                composable(CommunityCategories.HOT.name) {
                    CommunityPostsListBody(
                        savedSelectedSubCategory,
                        CommunityCategories.HOT
                    )
                }
                composable(CommunityCategories.NOTICE.name) {
                    CommunityPostsListBody(
                        savedSelectedSubCategory,
                        CommunityCategories.NOTICE
                    )
                }
            }

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
}

// 전체, 인기, 공지
@Composable
fun CommunityUpperCategory(
    currentSelectedCategory : CommunityCategories
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
                    start = Offset(this.size.width / 3f, this.size.height * 0.35f),
                    end = Offset(this.size.width / 3f, this.size.height * 0.75f),
                    strokeWidth = 4f
                )
                drawLine(
                    color = tertiary,
                    start = Offset((this.size.width * 2f) / 3f, this.size.height * 0.35f),
                    end = Offset((this.size.width * 2f) / 3f, this.size.height * 0.75f),
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
                        (this.size.width / 3f) * (0.1f + lowerLineStartPos),
                        this.size.height - 2f
                    ),
                    end = Offset(
                        (this.size.width / 3f) * (0.9f + lowerLineStartPos),
                        this.size.height - 2f
                    ),
                    strokeWidth = 4f
                )
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommunityCategories.entries.forEach { item ->
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
                            CommunityNavController.navigate(item.name)
                        }
                    }
            )
        }
    }
}

fun generateTest(count : Int) : List<Pair<String, String>> {
    val test = mutableListOf<Pair<String, String>>()

    for (idx : Int in 1..count) {
        test.add(
            Pair("코빈스의 이름은 코빈스의 이름은 코빈스의 이름은 코빈스의 이름은", "신준섭 신준섭 신준섭 신준섭 신준섭 신준섭")
        )
    }

    return test.toList()
}

@Composable
fun CommunityPostsListBody(
    savedSelectedSubCategory: Int,
    currentSelectedCategory: CommunityCategories
) {
    // 현재 선택 중인 서브 카테고리
    var currentSelectedSubCategory by rememberSaveable {
        mutableIntStateOf(savedSelectedSubCategory)
    }

    Column {
        if (currentSelectedCategory != CommunityCategories.NOTICE) {
            CommunityLowerCategory(
                currentSelectedCategory = currentSelectedCategory,
                savedSelectedSubCategory = currentSelectedSubCategory
            ) { getSelectedSubCategory ->
                currentSelectedSubCategory = getSelectedSubCategory
            }
        }

        CommunityPostsList(
            currentSelectedCategory,
            currentSelectedSubCategory
        )
    }
}

@Composable
fun CommunityLowerCategory(
    currentSelectedCategory: CommunityCategories,
    savedSelectedSubCategory: Int,
    callback : (Int) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val tertiary = MaterialTheme.colorScheme.tertiary
    var preSelectedSubCategory by rememberSaveable {
        mutableStateOf(savedSelectedSubCategory)
    }
    var currentSelectedSubCategory by rememberSaveable {
        mutableStateOf(savedSelectedSubCategory)
    }
    var _subCategoryLeftXPos by rememberSaveable { mutableFloatStateOf(0f) }
    val subCategoryLeftXPos by animateFloatAsState(
        targetValue = _subCategoryLeftXPos,
        animationSpec = tween(
            durationMillis = (currentSelectedSubCategory - preSelectedSubCategory).let {
                if (it > 0) { 600 }
                else if (it < 0){ 200 }
                else { 0 }
            }
        )
    )
    var _subCategoryRightXPos by rememberSaveable { mutableFloatStateOf(0f) }
    val subCategoryRightXPos by animateFloatAsState(
        targetValue = _subCategoryRightXPos,
        animationSpec = tween(
            durationMillis = (currentSelectedSubCategory - preSelectedSubCategory).let {
                if (it > 0) { 200 }
                else if (it < 0){ 600 }
                else { 0 }
            }
        )
    )
    var _subCategoryYPos by rememberSaveable { mutableFloatStateOf(0f) }
    var _subCategoryHeight by rememberSaveable { mutableStateOf(0f) }
    val subCategoryBackgroundColor by animateColorAsState(targetValue =
    if (currentSelectedSubCategory >= 0) { primary }
    else { onPrimary }
    ) {
        if (currentSelectedSubCategory < 0) {
            _subCategoryLeftXPos = 0f
            _subCategoryRightXPos = 0f
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .drawBehind {
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1f
                )
                drawOutline(
                    outline = Outline.Rounded(
                        roundRect = RoundRect(
                            left = 12.dp.toPx() + subCategoryLeftXPos.let {
                                if (preSelectedSubCategory == currentSelectedSubCategory) {
                                    _subCategoryLeftXPos
                                } else {
                                    it
                                }
                            },
                            right = 12.dp.toPx() + subCategoryRightXPos.let {
                                if (preSelectedSubCategory == currentSelectedSubCategory) {
                                    _subCategoryRightXPos
                                } else {
                                    it
                                }
                            },
                            top = 0f,
                            bottom = _subCategoryHeight,
                            cornerRadius = CornerRadius(100f, 100f)
                        )
                    ),
                    color = subCategoryBackgroundColor
                )
            }
            .padding(start = 12.dp)
    ) {
        currentSelectedCategory.list.forEach { item ->

            if (item.idx >= 0) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = dpToSp(dp = 14.dp),
                    color = if (item.idx == currentSelectedSubCategory) { onPrimary }
                    else { tertiary },
                    modifier = Modifier
                        .padding(end = 12.dp, bottom = 8.dp)
                        .border(
                            width = 1.dp,
                            color = if (item.idx == currentSelectedSubCategory) {
                                Color.Transparent
                            } else {
                                tertiary
                            },
                            shape = RoundedCornerShape(100)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) {

                            if (currentSelectedSubCategory != item.idx) {
                                preSelectedSubCategory = currentSelectedSubCategory

                                if (preSelectedSubCategory < 0) {
                                    preSelectedSubCategory = item.idx
                                }

                                currentSelectedSubCategory = item.idx
                                callback(item.idx)
                            } else {
                                // 전체 카테고리일 때만 서브 카테고리 해제 가능
                                if (currentSelectedCategory == CommunityCategories.ALL) {
                                    preSelectedSubCategory = -1
                                    currentSelectedSubCategory = -1
                                    callback(-1)
                                }
                            }
                        }
                        .onGloballyPositioned {
                            _subCategoryHeight = it.size.height.toFloat()

                            if (currentSelectedSubCategory == item.idx) {
                                _subCategoryLeftXPos = it.positionInParent().x
                                _subCategoryRightXPos =
                                    it.positionInParent().x + it.size.width.toFloat()
                                _subCategoryYPos = it.positionInParent().y
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CommunityPostsList(
    currentSelectedCategory: CommunityCategories,
    currentSelectedSubCategory: Int
) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    var postsList by rememberSaveable {
        mutableStateOf<List<Pair<String, String>>>(emptyList())
    }

    LaunchedEffect(currentSelectedSubCategory) {
        postsList = emptyList()
        delay(1000L)
        postsList = generateTest(20)
    }

    // 게시글이 없는 경우
    if (postsList.isEmpty()) {

        if (true) {

        }
        else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "게시글 없음",
                    fontSize = dpToSp(dp = 24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = postsList,
                key = { index : Int, item-> index }
            ) { index : Int, previewPost ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .drawWithContent {
                            drawContent()
                            drawLine(
                                color = tertiary,
                                start = Offset(0f, this.size.height),
                                end = Offset(this.size.width, this.size.height)
                            )
                        }
                        .padding(vertical = 12.dp)
                        .clickable {

                        }
                ) {
                    if (currentSelectedCategory != CommunityCategories.NOTICE) {
                        Row {
                            if (
                                currentSelectedCategory != CommunityCategories.HOT
                                && true
                            ) {
                                Text(
                                    text = "인기",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = dpToSp(dp = 12.dp),
                                    color = Color(0xFFAF1740),
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFFFB0B0),
                                            shape = RoundedCornerShape(15)
                                        )
                                        .padding(horizontal = 6.dp)
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                            }

                            if (currentSelectedSubCategory == -1
                                || currentSelectedCategory == CommunityCategories.HOT) {
                                Text(
                                    text = "카테고리",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = dpToSp(dp = 12.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .background(
                                            color = tertiary,
                                            shape = RoundedCornerShape(15)
                                        )
                                        .padding(horizontal = 6.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(4.dp))
                    }

                    // title
                    Text(
                        text = previewPost.first,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(0.75f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    // content
                    Text(
                        text = previewPost.second,
                        fontWeight = FontWeight.Medium,
                        fontSize = dpToSp(dp = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(0.75f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    // details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // posted time and view count
                        Text(
                            text = "0분 전 • 조회 999",
                            fontWeight = FontWeight.Medium,
                            fontSize = dpToSp(dp = 12.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        // like count and replies count
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_thumb_up_24),
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.scale(0.75f),
                                contentDescription = null
                            )
                            Text(
                                text = "999",
                                fontWeight = FontWeight.Medium,
                                fontSize = dpToSp(dp = 12.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.size(8.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.outline_comment_24),
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.scale(0.75f),
                                contentDescription = null
                            )
                            Text(
                                text = "999",
                                fontWeight = FontWeight.Medium,
                                fontSize = dpToSp(dp = 12.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }
}