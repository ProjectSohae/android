package com.sohae.feature.mypostlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.resource.R
import com.sohae.controller.navigation.main.MainNavGraphRoutes
import com.sohae.controller.navigation.main.MainNavGraphViewController
import com.sohae.domain.utils.getDiffTimeFromNow

@Composable
fun MyPostListView(
    myPostListViewModel: MyPostListViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
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
                .background(MaterialTheme.colorScheme.onPrimary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyPostListHeaderView()

            MyPostListBodyView(myPostListViewModel)
        }
    }
}

@Composable
fun MyPostListHeaderView() {
    val mainNavController = MainNavGraphViewController.mainNavController
    val tertiary = MaterialTheme.colorScheme.tertiary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .drawBehind {
                drawLine(
                    color = tertiary,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(start = 12.dp, end = 24.dp, top = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { mainNavController.popBackStack() },
            contentDescription = null
        )

        Text(
            text = "내 게시글",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MyPostListBodyView(myPostListViewModel: MyPostListViewModel) {
    val myPostsList = myPostListViewModel.myPostsList.collectAsState().value
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        myPostListViewModel.getMyPostsList(1) { msg, isSucceed ->
            if (!isSucceed) {
                com.sohae.controller.ui.snackbar.SnackBarController.show(msg, com.sohae.controller.ui.snackbar.SnackBarBehindTarget.VIEW)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState
    ) {
        itemsIndexed(
            items = myPostsList,
            key = { idx: Int, item -> idx }
        ) { idx: Int, item ->
            MyPostListItemView(item, myPostListViewModel)
        }
    }
}

@Composable
private fun MyPostListItemView(
    previewPost: PostEntity,
    myPostListViewModel: MyPostListViewModel
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
                mainNavController.currentBackStackEntry?.savedStateHandle?.set("selected_post_id", previewPost.id)
                mainNavController.navigate(MainNavGraphRoutes.POST.name)
            }
    ) {
        // category
        Text(
            text = myPostListViewModel.communityCategory[previewPost.categoryId.toInt()],
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
        Spacer(modifier = Modifier.size(4.dp))

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