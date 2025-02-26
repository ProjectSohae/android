package com.sohae.presentation.post

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.lifecycle.ViewModel
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.post.entity.PostEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import java.util.UUID

class PostViewModel: ViewModel() {

    private var _postDetails = MutableStateFlow<PostEntity?>(null)
    val postDetails = _postDetails.asStateFlow()

    private var _commentsList = MutableStateFlow<List<CommentEntity>>(emptyList())
    val commentsList = _commentsList.asStateFlow()

    val textFieldInteraction = MutableInteractionSource()

    private fun generatePostDetails(): PostEntity {

        return PostEntity(
            id = 0L,
            userId = UUID.randomUUID(),
            title = "test",
            content = "test",
            images = emptyList(),
            commentCount = 0,
            viewsCount = 0,
            likesCount = 0,
            bookmarksCount = 0,
            createdAt = Clock.System.now()
        )
    }

    fun getPostDetails(postId: Int) {
        _postDetails.value = generatePostDetails()
    }

    private fun generateComments(count: Int): List<CommentEntity> {
        val tmp = mutableListOf<CommentEntity>()

        for (idx: Int in 0..<count) {
            tmp.add(
                CommentEntity(
                    id = idx.toLong(),
                    postId = 0L,
                    userId = UUID.randomUUID(),
                    userName = "test",
                    parentCommentId = idx.toLong() % (count / 4),
                    content = "test",
                    createdAt = Clock.System.now()
                )
            )
        }

        return tmp
    }

    fun getCommentsList(postId: Int, offset: Int, count: Int) {

        _commentsList.value = (generateComments(count) + _commentsList.value).sortedWith(
            compareBy({ it.parentCommentId }, { it.id })
        )
    }

    fun uploadComment(content: String) {

    }
}