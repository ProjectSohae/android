package com.sohae.feature.community

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.resource.R
import com.sohae.controller.barcolor.BarColorController
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

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
            CommunityHeaderView()

            CommunityBodyView(
                savedStartDestination = CommunityCategory.ALL,
                communityViewModel = communityViewModel
            )
        }
    }
}

@Composable
private fun CommunityHeaderView() {
    val mainNavController = MainNavGraphViewController.mainNavController

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "커뮤니티",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        // search
        Icon(
            painter = painterResource(id = R.drawable.outline_search_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    mainNavController.navigate(MainNavGraphRoutes.SEARCHPOST.name)
                },
            contentDescription = null
        )
    }
}

@Composable
private fun CommunityBodyView(
    savedStartDestination : CommunityCategory,
    communityNavController : NavHostController = rememberNavController(),
    communityViewModel: CommunityViewModel
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val currentSelectedCategory = communityViewModel.selectedCategory.collectAsState().value
    var preSelectedCategory by rememberSaveable {
        mutableStateOf(CommunityCategory.ALL)
    }
    var transitionDir by rememberSaveable { mutableIntStateOf(1) }

    LaunchedEffect(currentSelectedCategory) {
        runBlocking {

            (currentSelectedCategory.idx - preSelectedCategory.idx).let {
                transitionDir = if (abs(it) > 0) { it / abs(it) } else { 0 }
            }

            preSelectedCategory = currentSelectedCategory
        }

        communityNavController.popBackStack()
        communityNavController.navigate(currentSelectedCategory.name)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommunityCategoryView(
            Modifier.zIndex(3f),
            communityViewModel
        )

        CommunitySubCategoryView(
            Modifier.zIndex(2f),
            communityViewModel
        )

        Box(
            modifier = Modifier.zIndex(1f),
            contentAlignment = Alignment.BottomEnd
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = communityNavController,
                startDestination = savedStartDestination.name,
                enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
                exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
            ) {
                composable(CommunityCategory.ALL.name) {
                    CommunityPostsListView(communityViewModel)
                }
                composable(CommunityCategory.HOT.name) {
                    CommunityPostsListView(communityViewModel)
                }
                composable(CommunityCategory.NOTICE.name) {
                    CommunityPostsListView(communityViewModel)
                }
            }

            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = (-12).dp, y = (-12).dp)
                    .clickable {
                        mainNavController.navigate(MainNavGraphRoutes.WRITEPOST.name)
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
private fun CommunityCategoryView(
    modifier: Modifier,
    communityViewModel: CommunityViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val selectedCategoryIdx = communityViewModel.selectedCategory.collectAsState().value.idx
    val lowerLineStartPos = animateFloatAsState(
        targetValue = selectedCategoryIdx.toFloat(),
        label = ""
    ).value

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = tertiary,
                    start = Offset(this.size.width / 3f, this.size.height * 0.35f),
                    end = Offset(this.size.width / 3f, this.size.height * 0.75f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = tertiary,
                    start = Offset((this.size.width * 2f) / 3f, this.size.height * 0.35f),
                    end = Offset((this.size.width * 2f) / 3f, this.size.height * 0.75f),
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
                        (this.size.width / 3f) * (0.1f + lowerLineStartPos),
                        this.size.height
                    ),
                    end = Offset(
                        (this.size.width / 3f) * (0.9f + lowerLineStartPos),
                        this.size.height
                    ),
                    strokeWidth = 2.dp.toPx()
                )
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommunityCategory.entries.forEach { item ->
            Text(
                text = item.categoryName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = selectedCategoryIdx.let {
                    if (it == item.idx) {
                        primary
                    } else { tertiary }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (selectedCategoryIdx != item.idx) {
                            communityViewModel.selectCategory(item)
                        }
                    }
            )
        }
    }
}

@Composable
private fun CommunitySubCategoryView(
    modifier: Modifier,
    communityViewModel: CommunityViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val tertiary = MaterialTheme.colorScheme.tertiary

    val currentSelectedCategory = communityViewModel.selectedCategory.collectAsState().value
    val currentSelectedSubCategoryIdx = communityViewModel.selectedSubCategory.collectAsState().value
    var preSelectedSubCategoryIdx by rememberSaveable {
        mutableIntStateOf(-1)
    }

    val composableScale by animateFloatAsState(
        targetValue = if (currentSelectedCategory == CommunityCategory.NOTICE) {
            0f
        } else { 1f },
        label = ""
    )

    val selectedSubCategoryBackgroundColor = if (preSelectedSubCategoryIdx < 0) {
        Color.Transparent
    } else {
        primary
    }

    var showSelectedSubCategoryBackground by rememberSaveable {
        mutableStateOf(false)
    }
    val selectedSubCategoryBackgroundScale by animateFloatAsState(
        targetValue = if (showSelectedSubCategoryBackground) {
            1f
        } else { 0f }
    )

    var selectedSubCategoryLeftXPos by rememberSaveable { mutableFloatStateOf(0f) }
    val animateLeftXPos by animateFloatAsState(
        targetValue = selectedSubCategoryLeftXPos,
        animationSpec = tween(
            durationMillis = (currentSelectedSubCategoryIdx - preSelectedSubCategoryIdx).let {
                if (preSelectedSubCategoryIdx < 0) {
                    0
                } else {
                    if (it > 0) { 600 }
                    else if (it < 0){ 200 }
                    else { 0 }
                }
            }
        ),
        label = ""
    ) {
        if (preSelectedSubCategoryIdx < 0) {
            preSelectedSubCategoryIdx = currentSelectedSubCategoryIdx

            if (currentSelectedSubCategoryIdx >= 0) {
                showSelectedSubCategoryBackground = true
            }
        }
    }

    var selectedSubCategoryRightXPos by rememberSaveable { mutableFloatStateOf(0f) }
    val animateRightXPos by animateFloatAsState(
        targetValue = selectedSubCategoryRightXPos,
        animationSpec = tween(
            durationMillis = (currentSelectedSubCategoryIdx - preSelectedSubCategoryIdx).let {
                if (preSelectedSubCategoryIdx < 0) {
                    0
                } else {
                    if (it > 0) { 200 }
                    else if (it < 0){ 600 }
                    else { 0 }
                }
            }
        ),
        label = ""
    ) {
        if (preSelectedSubCategoryIdx < 0) {
            preSelectedSubCategoryIdx = currentSelectedSubCategoryIdx

            if (currentSelectedSubCategoryIdx >= 0) {
                showSelectedSubCategoryBackground = true
            }
        }
    }

    LaunchedEffect(currentSelectedCategory) {

        if (currentSelectedCategory != CommunityCategory.HOT) {
            showSelectedSubCategoryBackground = false
            preSelectedSubCategoryIdx = -1
            selectedSubCategoryLeftXPos = -1f
            selectedSubCategoryRightXPos = -1f
        }
    }

    Box(
        modifier = modifier
            .drawBehind {
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1.dp.toPx(),
                    alpha = composableScale
                )
            }
            // measure: 측정할 composable, constraints: 부모로 부터 받은 layout 크기
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                layout(
                    placeable.width,
                    (placeable.height * composableScale).toInt()
                ) {
                    placeable.placeRelative(0, 0)
                }
            }
            .graphicsLayer {
                alpha = composableScale
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp)
        ) {
            // 테두리
            Row(modifier = Modifier.fillMaxWidth()) {
                currentSelectedCategory.subCategories.forEachIndexed { idx, item ->

                    Text(
                        text = item,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = onPrimary,
                        modifier = Modifier
                            .padding(end = 12.dp, bottom = 8.dp)
                            .border(
                                width = 1.dp,
                                color = tertiary,
                                shape = RoundedCornerShape(100)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawOutline(
                            outline = Outline.Rounded(
                                roundRect = RoundRect(
                                    left = animateLeftXPos + if (
                                        showSelectedSubCategoryBackground
                                    ) {
                                        (animateRightXPos - animateLeftXPos) * (0.5f * (1f - selectedSubCategoryBackgroundScale))
                                    } else { 0f },
                                    right = animateRightXPos - if (
                                        showSelectedSubCategoryBackground
                                    ) {
                                        (animateRightXPos - animateLeftXPos) * (0.5f * (1f - selectedSubCategoryBackgroundScale))
                                    } else { 0f },
                                    top = this.size.height * (0.5f - (0.5f * selectedSubCategoryBackgroundScale)),
                                    bottom = this.size.height * (0.5f + (0.5f * selectedSubCategoryBackgroundScale)),
                                    cornerRadius = CornerRadius(100f, 100f)
                                )
                            ),
                            color = selectedSubCategoryBackgroundColor,
                        )
                    }
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                currentSelectedCategory.subCategories.forEachIndexed { idx, item ->

                    Text(
                        text = item,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = if (idx == currentSelectedSubCategoryIdx) {
                            onPrimary
                        } else { tertiary },
                        modifier = Modifier
                            .padding(end = 12.dp, bottom = 8.dp)
                            .onGloballyPositioned {
                                if (currentSelectedSubCategoryIdx == idx) {
                                    selectedSubCategoryLeftXPos = it.positionInParent().x
                                    selectedSubCategoryRightXPos =
                                        selectedSubCategoryLeftXPos + it.size.width
                                }
                            }
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {

                                if (currentSelectedSubCategoryIdx == idx) {

                                    if (currentSelectedCategory != CommunityCategory.HOT) {
                                        communityViewModel.selectSubCategory(-1)
                                        showSelectedSubCategoryBackground = false
                                        preSelectedSubCategoryIdx = -1
                                        selectedSubCategoryLeftXPos = -1f
                                        selectedSubCategoryRightXPos = -1f
                                    }
                                } else {
                                    preSelectedSubCategoryIdx = currentSelectedSubCategoryIdx
                                    communityViewModel.selectSubCategory(idx)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityPostsListView(
    communityViewModel: CommunityViewModel
) {
    var isReadyPostsList by rememberSaveable { mutableStateOf(false) }
    val currentSelectedCategory = communityViewModel.selectedCategory.collectAsState().value
    val currentSelectedSubCategory = communityViewModel.selectedSubCategory.collectAsState().value
    val previewPostsList = communityViewModel.previewPostsList.collectAsState().value

    // 게시글이 없는 경우
    if (previewPostsList.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "게시글 없음",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = previewPostsList,
                key = { index : Int, item -> index }
            ) { index : Int, previewPost ->

                Column {
                    CommunityPostsListItemView(
                        currentSelectedCategory = currentSelectedCategory,
                        currentSelectedSubCategory = currentSelectedSubCategory,
                        previewPost = previewPost
                    )

                    // 광고
                    if ((index + 1) % 5 == 0) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun CommunityPostsListItemView(
    currentSelectedCategory: CommunityCategory,
    currentSelectedSubCategory: Int,
    previewPost: PostEntity
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val tertiary = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(vertical = 12.dp)
            .clickable {
                mainNavController.currentBackStackEntry?.savedStateHandle?.set("pressed_post_id", 0)
                mainNavController.navigate(MainNavGraphRoutes.POST.name)
            }
    ) {
        if (currentSelectedCategory != CommunityCategory.NOTICE) {
            Row {
                if (
                    currentSelectedCategory != CommunityCategory.HOT
                ) {
                    Text(
                        text = "인기",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
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
                    || currentSelectedCategory == CommunityCategory.HOT
                ) {
                    Text(
                        text = "카테고리",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
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
            text = previewPost.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(0.75f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.size(4.dp))

        // content
        Text(
            text = previewPost.content,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
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
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary
            )

            // like count and replies count
            Row {
                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_thumb_up_24),
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(18.dp),
                        contentDescription = null
                    )
                    Text(
                        text = " 999",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_comment_24),
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(18.dp),
                        contentDescription = null
                    )
                    Text(
                        text = " 999",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}