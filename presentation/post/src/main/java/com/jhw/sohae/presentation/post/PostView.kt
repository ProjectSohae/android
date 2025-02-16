package com.jhw.sohae.presentation.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jhw.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.jhw.sohae.common.ui.custom.snackbar.SnackBarController
import com.jhw.sohae.controller.barcolor.BarColorController
import com.jhw.sohae.controller.mainnavgraph.MainNavController
import com.jhw.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.jhw.sohae.presentation.postoption.PostOptionsView
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.user.entity.UserId

@Composable
fun PostView(
    postId: Int?,
    postViewModel: PostViewModel = viewModel()
) {
    val postDetails = postViewModel.postDetails.collectAsState().value
    val commentsList = postViewModel.commentsList.collectAsState().value
    var reloadComments by rememberSaveable { mutableStateOf(true) }
    var offset by rememberSaveable { mutableIntStateOf(0) }
    val count = 5

    BarColorController.setNavigationBarColor(MaterialTheme.colorScheme.onPrimary)

    if (postId == null) {
        SnackBarController.show("잘못된 게시글 접근입니다.", SnackBarBehindTarget.VIEW)
        MainNavController.popBack()
    } else {

        // 게시글 로딩
        LaunchedEffect(Unit) {
            postViewModel.getPostDetails(postId)
        }

        // 댓글 목록 로딩
        LaunchedEffect(reloadComments) {

            if (reloadComments) {
                reloadComments = false
                postViewModel.getCommentsList(postId, offset, count)
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

                } else {
                    PostViewHeader(postId, postDetails.userId)

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            PostDetailsView(postDetails)
                        }

                        itemsIndexed(
                            items = commentsList,
                            key = { idx: Int, commentDetails: CommentEntity -> idx }
                        ) { idx: Int, commentDetails: CommentEntity ->
                            CommentView(
                                postDetails.userId,
                                commentDetails,
                                idx != 0
                            )
                        }
                    }

                    PostViewFooter(
                        uploadComment = { getCommentContent ->
                            reloadComments = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PostViewHeader(
    postId: Int,
    posterUUID: UserId
) {
    var showOptionsDialog by remember { mutableStateOf(false) }

    if (showOptionsDialog) {
        PostOptionsView(
            postId = postId,
            posterNickname = "닉네임",
            onDismissRequest = { showOptionsDialog = false },
            onConfirm = { showOptionsDialog = false }
        )
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
            modifier = Modifier.clickable { MainNavController.popBack() },
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
    postDetails: PostEntity?
) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    var likeThisPost by remember { mutableStateOf(false) }
    var bookmarkThisPost by remember { mutableStateOf(false) }

    if (postDetails != null) {
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
                                    MainNavController.navigate(MainNavGraphRoutes.POSTIMAGE.name)
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
                text = "${postDetails.userId} • ${postDetails.createdAt}",
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
    }

    // 배너 광고
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = MaterialTheme.colorScheme.tertiary
    ) {}
}

@Composable
private fun CommentView(
    posterUUID: UserId,
    commentDetails: CommentEntity,
    drawDivider: Boolean
) {
    val tertiary = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (drawDivider) {
                    drawLine(
                        color = tertiary,
                        start = Offset(24.dp.toPx(), 0f),
                        end = Offset(this.size.width - 24.dp.toPx(), 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (true) {
                    Text(
                        text = "작성자",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(100))
                            .padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "${commentDetails.userName} • ${commentDetails.createdAt}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.baseline_more_vert_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = null
                ) {

                }
            )
        }

        Text(
            text = commentDetails.content,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
//            commentDetails.replyList.forEach { getReplyDetails ->
//                ReplyView(posterNickname, getReplyDetails)
//            }
        }
    }
}

@Composable
private fun ReplyView(
    posterNickname: String,
    replyDetails: CommentEntity
) {
    Row(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_subdirectory_arrow_right_24),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .fillMaxWidth()
                .background(
                    Color(
                        ColorUtils.blendARGB(
                            MaterialTheme.colorScheme.tertiary.toArgb(),
                            MaterialTheme.colorScheme.onPrimary.toArgb(),
                            0.75f
                        )
                    )
                )
                .padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (posterNickname == replyDetails.userName) {
                        Text(
                            text = "작성자",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "${replyDetails.userName} • ${replyDetails.createdAt}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.baseline_more_vert_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = null
                    ) {

                    }
                )
            }

            Text(
                text = replyDetails.content,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostViewFooter(
    uploadComment: (String) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val brightTertiary = Color(
        ColorUtils.blendARGB(
            MaterialTheme.colorScheme.tertiary.toArgb(),
            MaterialTheme.colorScheme.onPrimary.toArgb(),
            0.75f
        )
    )
    val interactionSource = remember { MutableInteractionSource() }
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center
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
                                    indication = null,
                                    interactionSource = null
                                ) {
                                    if (commentContent.isNotBlank()) {
                                        uploadComment(commentContent)
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