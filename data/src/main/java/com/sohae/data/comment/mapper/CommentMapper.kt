package com.sohae.data.comment.mapper

import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.comment.request.CreateCommentRequest
import com.sohae.common.models.comment.response.CommentResponse
import kotlinx.datetime.Instant

fun CommentEntity.toCreateCommentRequest(): CreateCommentRequest = CreateCommentRequest(
    this.postId,
    this.parentCommentId,
    this.content
)

fun CommentResponse.toCommentEntity(): CommentEntity = CommentEntity(
    this.id,
    this.postId,
    this.userId,
    this.userName,
    this.parentCommentId ?: this.id,
    this.content,
    Instant.fromEpochMilliseconds(this.createdAt)
)