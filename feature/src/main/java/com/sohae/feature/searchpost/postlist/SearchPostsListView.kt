package com.sohae.feature.searchpost.postlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.CircularLoadingBarView
import com.sohae.controller.navigation.main.MainNavGraphRoutes
import com.sohae.controller.navigation.main.MainNavGraphViewController
import com.sohae.domain.utils.getDiffTimeFromNow
import com.sohae.feature.community.category.CommunityCategory

@Composable
fun SearchPostsListView(
    keyword: String,
    searchPostsListViewModel: SearchPostsListViewModel
) {
    val isReadyPostsList = searchPostsListViewModel.isReadyPostsList.collectAsState().value
    val previewPostsList = searchPostsListViewModel.previewPostsList.collectAsState().value
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val blankScreen = @Composable {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "게시글 없음",
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect(firstVisibleItemIndex) {

        if (keyword.isNotBlank()) {

            if (firstVisibleItemIndex >= previewPostsList.size / 2) {
                searchPostsListViewModel.getPreviewPostsListByKeyword(keyword) { msg ->
                    com.sohae.controller.ui.snackbar.SnackBarController.show(msg, com.sohae.controller.ui.snackbar.SnackBarBehindTarget.VIEW)
                }
            }
        }
    }

    if (previewPostsList.isEmpty()) {

        if (keyword.isBlank()) {
            blankScreen()
        } else {

            if (isReadyPostsList) {
                blankScreen()
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
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            itemsIndexed(
                items = previewPostsList,
                key = { idx, item -> idx }
            ) { idx, item ->
                SearchPostsListItemView(item)
            }
        }
    }
}

@Composable
private fun SearchPostsListItemView(
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
        Row {
            if (previewPost.likesCount >= 10) {
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