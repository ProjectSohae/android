package com.sohae.domain.comment.usecase

import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.comment.entity.CommentId
import com.sohae.common.models.post.entity.PostId
import com.sohae.domain.comment.reposiotory.CommentRepository
import javax.inject.Inject

class CommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {

    fun createComment(
        commentEntity: CommentEntity,
        callback: (Boolean) -> Unit
    ) {
        commentRepository.createComment(commentEntity) {
            callback(it)
        }
    }

    fun getCommentsList(
        postId: PostId,
        page: Int,
        callback: (List<CommentEntity>, Boolean) -> Unit
    ) {
        commentRepository.getCommentsList(postId, page) { getCommentsList, isSucceed ->
            callback(getCommentsList, isSucceed)
        }
    }

    fun deleteComment(
        commentId: CommentId,
        callback: (Boolean) -> Unit
    ) {
        commentRepository.deleteComment(commentId) {
            callback(it)
        }
    }
}