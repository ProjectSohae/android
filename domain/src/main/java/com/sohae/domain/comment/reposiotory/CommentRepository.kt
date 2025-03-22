package com.sohae.domain.comment.reposiotory

import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.comment.entity.CommentId
import com.sohae.common.models.post.entity.PostId

interface CommentRepository {

    fun createComment(
        commentEntity: CommentEntity,
        callback: (Boolean) -> Unit
    )

    fun getCommentsList(
        postId: PostId,
        page: Int,
        callback: (List<CommentEntity>, Boolean) -> Unit
    )

    fun deleteComment(
        commentId: CommentId,
        callback: (Boolean) -> Unit
    )
}