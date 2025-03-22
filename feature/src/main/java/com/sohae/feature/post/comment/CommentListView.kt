package com.sohae.feature.post.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.user.entity.UserId
import com.sohae.common.resource.R
import com.sohae.domain.utils.getDiffTimeFromNow
import com.sohae.feature.post.commentoption.CommentOptionView
import com.sohae.feature.post.post.PostViewModel

@Composable
fun CommentListView(
    postDetails: PostEntity,
    commentListViewModel: CommentListViewModel
) {
    var showCommentOption by remember { mutableStateOf(false) }
    var selectedCommentEntity by remember { mutableStateOf<CommentEntity?>(null) }
    val commentsList = commentListViewModel.commentsList.collectAsState().value

    if (showCommentOption) {

        if (selectedCommentEntity != null) {
            CommentOptionView(
                selectedCommentEntity!!.userName,
                onDismissRequest = {
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

                        }
                    }
                    showCommentOption = false
                }
            )
        } else {
            showCommentOption = false
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
                    onClick = {
                        selectedCommentEntity = commentEntity
                        showCommentOption = true
                    }
                )
            } else {
                ReplyView(
                    posterId = postDetails.userId,
                    replyDetails = commentEntity,
                    onClick = {
                        selectedCommentEntity = commentEntity
                        showCommentOption = true
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
    onClick: (CommentEntity) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val tertiary = MaterialTheme.colorScheme.tertiary

    LaunchedEffect(isPressed) {

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = if (drawDivider) {
                    16.dp
                } else {
                    0.dp
                }
            )
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
            .padding(top = 16.dp)
            .clickable { isPressed = true }
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
                if (commentDetails.userId == posterId) {
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
                    text = "${commentDetails.userName} • ${getDiffTimeFromNow(commentDetails.createdAt)}",
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
                    onClick(commentDetails)
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
        }
    }
}

@Composable
private fun ReplyView(
    posterId: UserId,
    replyDetails: CommentEntity,
    onClick: (CommentEntity) -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 12.dp, start = 24.dp, end = 24.dp)
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
                    if (posterId == replyDetails.userId) {
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
                        text = "${replyDetails.userName} • ${getDiffTimeFromNow(replyDetails.createdAt)}",
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
                        onClick(replyDetails)
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