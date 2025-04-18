package com.sohae.feature.community.communitypostlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.CircularLoadingBarView
import com.sohae.controller.navigation.main.MainNavGraphRoutes
import com.sohae.controller.navigation.main.MainNavGraphViewController
import com.sohae.controller.ui.snackbar.SnackBarBehindTarget
import com.sohae.controller.ui.snackbar.SnackBarController
import com.sohae.domain.utils.getDiffTimeFromNow
import com.sohae.feature.community.category.CommunityCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CommunityPostListView(
    communityNavController: NavHostController,
    thisBackStackEntryId: String,
    currentSelectedCategory: CommunityCategory,
    currentSelectedSubCategoryIdx: Int,
    communityPostListViewModel: CommunityPostListViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val currentBackStackEntryId = communityNavController.currentBackStackEntryAsState().value?.id
    val currentJob = communityPostListViewModel.currentJob.collectAsState().value
    val pageOffset = communityPostListViewModel.PAGE_OFFSET
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex
                .let { if (it < 0) { 0 } else { it } }
        }
    }
    val maxFirstVisibleItemIndex = communityPostListViewModel.maxFirstVisibleItemIndex.collectAsState().value
    val isReadyPostsList = communityPostListViewModel.isReadyPostsList.collectAsState().value
    val previewPostsList = communityPostListViewModel.previewPostsList.collectAsState().value

    // debounce 적용
    DisposableEffect(firstVisibleItemIndex, currentBackStackEntryId) {

        if (thisBackStackEntryId != currentBackStackEntryId) {
            communityPostListViewModel.setCurrentJob(null)
        } else {
            if (maxFirstVisibleItemIndex < firstVisibleItemIndex) {

                communityPostListViewModel.setMaxFirstVisibleItemIndex(
                    firstVisibleItemIndex + (pageOffset / 2)
                )

                val job = coroutineScope.launch {

                    delay(1000L)

                    communityPostListViewModel.getPreviewPostsList(
                        page = communityPostListViewModel.currentPage,
                        category = currentSelectedCategory,
                        subCategoryIdx = currentSelectedSubCategoryIdx
                    ) { msg: String, isSucceed: Boolean ->

                        if (!isSucceed) {
                            SnackBarController.show(msg, SnackBarBehindTarget.VIEW)
                        } else {
                            communityPostListViewModel.currentPage++
                        }
                    }
                }

                communityPostListViewModel.setCurrentJob(job)
            }
        }


        onDispose {
            currentJob?.cancel()
        }
    }

    // 게시글이 없는 경우
    if (previewPostsList.isEmpty()) {

        if (isReadyPostsList) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                CircularLoadingBarView()
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary),
            state = lazyListState
        ) {
            itemsIndexed(
                items = previewPostsList,
                key = { index : Int, item -> index }
            ) { index : Int, previewPost ->

                Column {
                    CommunityPostsListItemView(
                        currentSelectedCategory = currentSelectedCategory,
                        currentSelectedSubCategory = currentSelectedSubCategoryIdx,
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
                mainNavController.currentBackStackEntry?.savedStateHandle?.set(
                    "selected_post_id",
                    previewPost.id
                )
                mainNavController.navigate(MainNavGraphRoutes.POST.name)
            }
    ) {
        if (currentSelectedCategory != CommunityCategory.NOTICE) {
            Row {
                if (
                    currentSelectedCategory != CommunityCategory.HOT
                    && previewPost.likesCount >= 10
                ) {
                    Text(
                        text = "인기",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color(0xFFAF1740),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .background(
                                color = Color(0xFFFFB0B0),
                                shape = RoundedCornerShape(15)
                            )
                            .padding(horizontal = 6.dp)
                    )
                }

                if (currentSelectedSubCategory == -1
                    || currentSelectedCategory == CommunityCategory.HOT
                ) {
                    Text(
                        text = CommunityCategory
                            .ALL.subCategories[previewPost.categoryId.toInt()],
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .background(
                                color = tertiary,
                                shape = RoundedCornerShape(15)
                            )
                            .padding(horizontal = 6.dp)
                    )
                }
            }
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
                text = "${getDiffTimeFromNow(previewPost.createdAt)} • 조회 ${previewPost.viewsCount}",
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
                        text = " ${previewPost.likesCount}",
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
                        text = " ${previewPost.commentCount}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}