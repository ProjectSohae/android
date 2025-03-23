package com.sohae.feature.post.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.comment.entity.CommentId
import com.sohae.common.models.post.entity.PostId
import com.sohae.domain.comment.usecase.CommentUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val commentUseCase: CommentUseCase
): ViewModel() {

    val myAccount = myInfoUseCase.getMyAccount().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    private var _commentsList = MutableStateFlow<List<CommentEntity>>(emptyList())
    val commentsList = _commentsList.asStateFlow()

    fun getCommentsList(
        postId: PostId,
        page: Int,
    ) {
        commentUseCase.getCommentsList(postId, page) { getCommentsList, isSucceed ->

            if (isSucceed) {
                _commentsList.value += getCommentsList
            } else {

            }
        }
    }

    fun deleteComment(
        commentId: CommentId
    ) {
        commentUseCase.deleteComment(commentId) {

        }
    }
}