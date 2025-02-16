package com.jhw.sohae.presentation.post

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

    fun getPostDetails(postId: Int) {
        _postDetails.value = PostEntity(
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

    fun getCommentsList(postId: Int, offset: Int, count: Int) {
        val tmp = mutableListOf<CommentEntity>()

        for (idx: Int in 0..10) {
            tmp.add(
                CommentEntity(
                    id = 0L,
                    postId = 0L,
                    userId = UUID.randomUUID(),
                    userName = "test",
                    parentCommentId = 0L,
                    content = "test",
                    createdAt = Clock.System.now()
                )
            )
        }

        _commentsList.value = tmp.toList()
    }
}