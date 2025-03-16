package com.sohae.feature.community

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sohae.common.resource.R
import com.sohae.controller.barcolor.BarColorController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.domain.community.category.CommunityCategory
import com.sohae.feature.community.allpostlist.AllPostNavView
import com.sohae.feature.community.hotpostlist.HotPostNavView
import com.sohae.feature.community.noticelist.NoticePostNavVew
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

            CommunityBodyView(communityViewModel)
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
    communityViewModel: CommunityViewModel,
    communityNavController : NavHostController = rememberNavController(),
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val currentSelectedCategory = communityNavController.currentBackStackEntryAsState().value?.destination?.route
    var preSelectedCategory by rememberSaveable {
        mutableStateOf(CommunityCategory.ALL)
    }
    var transitionDir by rememberSaveable { mutableIntStateOf(1) }

    LaunchedEffect(currentSelectedCategory) {

        if (currentSelectedCategory != null) {
            (
                    CommunityCategory.valueOf(currentSelectedCategory).idx
                            - preSelectedCategory.idx
            ).let {
                transitionDir = if (abs(it) > 0) { it / abs(it) } else { 0 }
            }

            preSelectedCategory = CommunityCategory.valueOf(currentSelectedCategory)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommunityCategoryView(
            Modifier.zIndex(3f),
            communityNavController,
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
                startDestination = CommunityCategory.ALL.name,
                enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
                exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
            ) {
                composable(CommunityCategory.ALL.name) {
                    AllPostNavView(communityViewModel)
                }
                composable(CommunityCategory.HOT.name) {
                    HotPostNavView(communityViewModel)
                }
                composable(CommunityCategory.NOTICE.name) {
                    NoticePostNavVew(communityViewModel)
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
    communityNavController: NavHostController,
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
                            communityNavController.navigate(item.name) {
                                communityNavController.popBackStack()
                                communityViewModel.selectCategory(item)
                            }
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
                                    } else {
                                        0f
                                    },
                                    right = animateRightXPos - if (
                                        showSelectedSubCategoryBackground
                                    ) {
                                        (animateRightXPos - animateLeftXPos) * (0.5f * (1f - selectedSubCategoryBackgroundScale))
                                    } else {
                                        0f
                                    },
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