package com.sohae.feature.post.post

import android.util.Log
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.comment.entity.CommentId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.comment.usecase.CommentUseCase
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.post.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase,
    private val commentUseCase: CommentUseCase
): ViewModel() {

    val myAccount = myInfoUseCase.getMyAccount().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    private var _postDetails = MutableStateFlow<PostEntity?>(null)
    val postDetails = _postDetails.asStateFlow()

    private var _parentCommentId = MutableStateFlow<CommentId?>(null)
    val parentCommentId = _parentCommentId.asStateFlow()

    var parentCommentUsername = ""

    val textFieldFocusRequester = FocusRequester()

    fun activeTextField() {
        textFieldFocusRequester.requestFocus()
    }

    fun initParentCommentId() {
        parentCommentUsername = ""
        _parentCommentId.value = null
    }

    fun setParentCommentId(input: CommentId) {
        _parentCommentId.value = input
    }

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

    fun createComment(
        postDetails: PostEntity?,
        myAccount: MyAccountEntity?,
        commentContent: String
    ) {

        if (postDetails == null || myAccount == null) {
            return
        }

        val commentEntity = CommentEntity(
            0,
            postDetails.id,
            myAccount.id,
            myAccount.username,
            parentCommentId.value,
            commentContent,
            Clock.System.now()
        )

        commentUseCase.createComment(commentEntity) {
            initParentCommentId()
        }
    }
}