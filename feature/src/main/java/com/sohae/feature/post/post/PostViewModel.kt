package com.sohae.feature.post.post

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.community.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase
): ViewModel() {

    val myAccount = myInfoUseCase.getMyAccount().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    private var _postDetails = MutableStateFlow<PostEntity?>(null)
    val postDetails = _postDetails.asStateFlow()

    private var _commentsList = MutableStateFlow<List<CommentEntity>>(emptyList())
    val commentsList = _commentsList.asStateFlow()

    val textFieldInteraction = MutableInteractionSource()

    fun getPostDetails(
        postId: Long,
        callback: (Boolean) -> Unit
    ) {

        postUseCase.getPostDetails(
            postId
        ) { getPostDetails, isSucceed ->

            if (isSucceed) {
                _postDetails.value = getPostDetails
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun deletePost(
        postId: Long,
        callback: (Boolean) -> Unit
    ) {

        postUseCase.deletePost(postId) { isSucceed ->
            callback(isSucceed)
        }
    }

    private fun createComments(count: Int): List<CommentEntity> {
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

        _commentsList.value = (createComments(count) + _commentsList.value).sortedWith(
            compareBy({ it.parentCommentId }, { it.id })
        )
    }

    fun uploadComment(content: String) {

    }
}