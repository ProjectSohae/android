package com.sohae.feature.post.post

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.user.entity.UserId
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.composable.CircularLoadingBarView
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.controller.barcolor.BarColorController
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.feature.post.commentoption.CommentOptionView
import com.sohae.feature.post.option.PostOptionView
import com.sohae.domain.utils.getDiffTimeFromNow
import com.sohae.feature.post.comment.CommentListView
import com.sohae.feature.post.comment.CommentListViewModel

@Composable
fun PostView(
    postId: Long?,
    postViewModel: PostViewModel,
    commentListViewModel: CommentListViewModel
) {
    val mainNavController = MainNavGraphViewController.mainNavController
    val postDetails = postViewModel.postDetails.collectAsState().value

    BarColorController.setNavigationBarColor(MaterialTheme.colorScheme.onPrimary)

    if (postId == null) {
        SnackBarController.show("잘못된 게시글 접근입니다.", SnackBarBehindTarget.VIEW)
        mainNavController.popBackStack()
    } else {

        // 게시글 로딩
        LaunchedEffect(Unit) {
            postViewModel.getPostDetails(postId) { isSucceed ->

                if (!isSucceed) {
                    SnackBarController.show(
                        "존재하지 않는 게시글입니다.",
                        SnackBarBehindTarget.VIEW
                    )
                    mainNavController.popBackStack()
                }
            }
        }

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
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(innerPadding)
            ) {

                if (postDetails == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularLoadingBarView()
                    }
                } else {
                    PostHeaderView(postViewModel)

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            PostDetailsView(postViewModel)
                        }

                        item {
                            CommentListView(
                                postViewModel.postDetails.value!!,
                                commentListViewModel
                            )
                        }
                    }

                    PostViewFooter(postViewModel)
                }
            }
        }
    }
}

@Composable
private fun PostHeaderView(
    postViewModel: PostViewModel
) {
    val myAccount = postViewModel.myAccount.collectAsState().value
    val postDetails = postViewModel.postDetails.collectAsState().value!!
    val mainNavController = MainNavGraphViewController.mainNavController
    var showOptionsDialog by remember { mutableStateOf(false) }

    if (showOptionsDialog) {

        if (myAccount != null) {
            PostOptionView(
                isMyPost = postDetails.userId == myAccount.id,
                onDismissRequest = { showOptionsDialog = false },
                onConfirm = { selectedOptionIdx ->
                    showOptionsDialog = false

                    when (selectedOptionIdx) {
                        // 작성자 차단
                        0 -> {

                        }
                        // 게시글 신고
                        1 -> {

                        }
                        // 게시글 수정
                        2 -> {
                            mainNavController.currentBackStackEntry?.savedStateHandle
                                ?.set("modify_post_id", postDetails.id)
                            mainNavController.navigate(MainNavGraphRoutes.WRITEPOST.name)
                        }
                        // 게시글 삭제
                        3 -> {
                            postViewModel.deletePost(postDetails.id) { isSucceed ->

                                if (isSucceed) {
                                    SnackBarController.show(
                                        "게시글 삭제 성공",
                                        SnackBarBehindTarget.VIEW
                                    )
                                    mainNavController.popBackStack()
                                } else {
                                    SnackBarController.show(
                                        "게시글 삭제 실패하였습니다.\n잠시 후 다시 시도해주세요.",
                                        SnackBarBehindTarget.VIEW
                                    )
                                }
                            }
                        }
                    }
                }
            )
        } else {
            SnackBarController.show(
                "나중에 다시 시도해 주세요.",
                SnackBarBehindTarget.VIEW
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { mainNavController.popBackStack() },
            contentDescription = null
        )

        Text(
            text = "게시글",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Icon(
            painter = painterResource(id = R.drawable.baseline_more_vert_24),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(28.dp)
                .clickable { showOptionsDialog = true },
            contentDescription = null
        )
    }
}

@Composable
private fun PostDetailsView(
    postViewModel: PostViewModel
) {
    val postDetails = postViewModel.postDetails.collectAsState().value!!
    val mainNavController = MainNavGraphViewController.mainNavController
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    var likeThisPost by remember { mutableStateOf(false) }
    var bookmarkThisPost by remember { mutableStateOf(false) }

    // 글 제목
    Text(
        text = postDetails.title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
    )

    // 글 내용
    Text(
        text = postDetails.content,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
    )

    // 글 사진목록
    if (postDetails.images.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 12.dp)
        ) {
            item {
                postDetails.images.forEach {
                    Image(
                        painter = painterResource(id = R.drawable.yoon),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(120.dp)
                            .clip(RoundedCornerShape(10))
                            .clickable {
                                mainNavController.navigate(MainNavGraphRoutes.POSTIMAGE.name)
                            },
                        contentDescription = null
                    )
                }
            }
        }
    }

    // 작성자, 글 작성 시간
    Row(
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
            .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 24.dp)
    ) {
        Text(
            text = "${postDetails.username} • ${getDiffTimeFromNow(postDetails.createdAt)}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }

    // 추천, 댓글, 스크랩
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 추천
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { likeThisPost = !likeThisPost },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (likeThisPost) {
                        R.drawable.baseline_thumb_up_24
                    } else { R.drawable.baseline_thumb_up_off_alt_24 }
                ),
                tint = if (likeThisPost) { primary } else { tertiary },
                contentDescription = null
            )

            Text(
                text = " 추천 ${postDetails.likesCount}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (likeThisPost) { primary } else { tertiary },
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }

        // 댓글
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_comment_24),
                tint = tertiary,
                contentDescription = null
            )

            Text(
                text = " 댓글 ${postDetails.commentCount}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                color = tertiary
            )
        }

        // 스크랩
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { bookmarkThisPost = !bookmarkThisPost },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = if (bookmarkThisPost) {
                        R.drawable.baseline_bookmark_24
                    } else { R.drawable.baseline_bookmark_border_24 }
                ),
                tint = if (bookmarkThisPost) { primary } else { tertiary },
                contentDescription = null
            )

            Text(
                text = " 스크랩 ${postDetails.bookmarksCount}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (bookmarkThisPost) { primary } else { tertiary },
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }

    // 배너 광고
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = MaterialTheme.colorScheme.tertiary
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostViewFooter(
    postViewModel: PostViewModel
) {
    val postDetails = postViewModel.postDetails.collectAsState().value
    val myAccountEntity = postViewModel.myAccount.collectAsState().value
    val primary = MaterialTheme.colorScheme.primary
    val brightTertiary = Color(
        ColorUtils.blendARGB(
            MaterialTheme.colorScheme.tertiary.toArgb(),
            MaterialTheme.colorScheme.onPrimary.toArgb(),
            0.75f
        )
    )
    val interactionSource = postViewModel.textFieldInteraction
    var commentContent by rememberSaveable { mutableStateOf("") }
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedContainerColor = brightTertiary,
        unfocusedContainerColor = brightTertiary,
        errorContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )

    val test = interactionSource.collectIsFocusedAsState().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 12.dp),
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = commentContent,
            onValueChange = { commentContent = it },
            enabled = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            interactionSource = interactionSource,
            maxLines = 4,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = commentContent,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    contentPadding = PaddingValues(start = 24.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    placeholder = {
                        Text(
                            text = "댓글을 입력하세요.",
                            fontSize = 16.sp,
                            color = primary,)
                    },
                    container = @Composable {
                        Container(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            modifier = Modifier,
                            colors = textFieldColors,
                            shape = RoundedCornerShape(25)
                        )
                    },
                    singleLine = true,
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = textFieldColors,
                    suffix = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_keyboard_double_arrow_right_24),
                            tint = if (commentContent.isBlank()) {
                                MaterialTheme.colorScheme.tertiary
                            } else { primary },
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    if (commentContent.isNotBlank()) {
                                        postViewModel.createComment(
                                            postDetails,
                                            myAccountEntity,
                                            commentContent
                                        )
                                        commentContent = ""
                                    }
                                }
                        )
                    }
                )
            }
        )
    }
}