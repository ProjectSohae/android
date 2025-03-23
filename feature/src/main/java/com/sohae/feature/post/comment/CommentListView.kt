package com.sohae.feature.post.comment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.user.entity.UserId
import com.sohae.common.resource.R
import com.sohae.common.ui.custom.snackbar.SnackBarBehindTarget
import com.sohae.common.ui.custom.snackbar.SnackBarController
import com.sohae.domain.utils.getDiffTimeFromNow
import com.sohae.feature.post.post.PostViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun CommentListView(
    postViewModel: PostViewModel,
    commentListViewModel: CommentListViewModel
) {
    val postDetails = postViewModel.postDetails.value!!
    val myAccount = commentListViewModel.myAccount.collectAsState().value
    var showCommentOption by remember { mutableStateOf(false) }
    var selectedCommentEntity by remember { mutableStateOf<CommentEntity?>(null) }
    val commentsList = commentListViewModel.commentsList.collectAsState().value

    LaunchedEffect(Unit) {
        commentListViewModel.getCommentsList(postDetails.id, 1)
    }

    if (selectedCommentEntity != null) {

        if (showCommentOption) {
            CommentOptionView(
                selectedCommentEntity!!.userId == myAccount!!.id,
                onDismissRequest = {
                    selectedCommentEntity = null
                    showCommentOption = false
                },
                onConfirm = {
                    when (it) {
                        // 작성자 차단
                        0 -> {

                        }
                        // 댓글 신고
                        1 -> {

                        }
                        // 댓글 삭제
                        2 -> {
                            commentListViewModel.deleteComment(selectedCommentEntity!!.id)
                        }
                    }
                    selectedCommentEntity = null
                    showCommentOption = false
                }
            )
        } else {
            postViewModel.parentCommentUsername = selectedCommentEntity!!.userName
            postViewModel.setParentCommentId(selectedCommentEntity!!.id)
            postViewModel.activeTextField()
            selectedCommentEntity = null
        }
    }

    LaunchedEffect(Unit) {

    }

    Column {

        commentsList.forEachIndexed { idx: Int, commentEntity ->

            if (commentEntity.id == commentEntity.parentCommentId) {
                CommentListItemView(
                    posterId = postDetails.userId,
                    commentDetails = commentEntity,
                    drawDivider = idx != 0,
                    onClick = { entity, showOption ->
                        selectedCommentEntity = entity
                        showCommentOption = showOption
                    }
                )
            } else {
                ReplyView(
                    posterId = postDetails.userId,
                    replyDetails = commentEntity,
                    onClick = { entity, showOption ->
                        selectedCommentEntity = entity
                        showCommentOption = showOption
                    }
                )
            }
        }
    }
}

@Composable
private fun CommentListItemView(
    posterId: UserId,
    commentDetails: CommentEntity,
    drawDivider: Boolean,
    onClick: (CommentEntity, Boolean) -> Unit
) {
    val isDeleted = commentDetails.userName.isBlank()
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
            .clickable { if (!isDeleted) { onClick(commentDetails, false) } }
    ) {
        if (!isDeleted) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 24.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (posterId == commentDetails.userId) {
                        Text(
                            text = "작성자",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(100)
                                )
                                .padding(horizontal = 12.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = commentDetails.userName,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        modifier = Modifier.fillMaxWidth(0.5f).padding(start = 8.dp)
                    )

                    Text(
                        text = " • ${getDiffTimeFromNow(commentDetails.createdAt)}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.baseline_more_vert_24),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) { onClick(commentDetails, true) }
                )
            }

            Text(
                text = commentDetails.content,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        } else {
            Text(
                text = "삭제된 댓글입니다.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize().padding(24.dp, 20.dp)
            )
        }
    }
}

@Composable
private fun ReplyView(
    posterId: UserId,
    replyDetails: CommentEntity,
    onClick: (CommentEntity, Boolean) -> Unit
) {
    val isDeleted = replyDetails.userName.isBlank()

    Row(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
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
            if (!isDeleted) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (posterId == replyDetails.userId) {
                            Text(
                                text = "작성자",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(100)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = replyDetails.userName,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            modifier = Modifier.fillMaxWidth(0.5f).padding(start = 8.dp)
                        )

                        Text(
                            text = " • ${getDiffTimeFromNow(replyDetails.createdAt)}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) { onClick(replyDetails, true) }
                    )
                }

                Text(
                    text = replyDetails.content,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
                )
            } else {
                Text(
                    text = "삭제된 댓글입니다.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize().padding(24.dp, 20.dp)
                )
            }
        }
    }
}