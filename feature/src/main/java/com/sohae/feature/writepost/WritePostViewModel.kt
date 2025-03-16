package com.sohae.feature.writepost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.post.entity.PostImageEntity
import com.sohae.domain.community.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.sohae.domain.community.category.CommunityCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase
): ViewModel() {

    var postId: Long? = null

    private var postDetails: PostEntity? = null

    private var _isReadyPostDetails = MutableStateFlow(false)
    val isReadyPostDetails = _isReadyPostDetails.asStateFlow()

    val categoryList = CommunityCategory.ALL.subCategories

    val myId = myInfoUseCase.getMyAccount().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    private var _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private var _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private var _category = MutableStateFlow(-1)
    val category = _category.asStateFlow()

    private var _selectedImageList = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageList = _selectedImageList.asStateFlow()

    fun setTitle(input: String) {
        _title.value = input
    }

    fun setContent(input: String) {
        _content.value = input
    }

    fun setCategory(input: Int) {
        _category.value = input
    }

    fun setSelectedImageList(input: List<Uri>) {
        _selectedImageList.value = input
    }

    fun setIsReadyPostDetails(input: Boolean) {
        _isReadyPostDetails.value = input
    }

    fun loadPostDetails(
        callback: (Boolean) -> Unit
    ) {

        postUseCase.getPostDetails(postId!!) { getPostDetails, isSucceed ->

            if (getPostDetails != null) {
                setTitle(getPostDetails.title)
                setContent(getPostDetails.content)
                setCategory(getPostDetails.categoryId.toInt())
                postDetails = getPostDetails
                //setSelectedImageList(postDetails.images)
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun updatePost(
        myId: UUID,
        callback: (Boolean) -> Unit
    ) {

        postUseCase.updatePost(
            PostEntity(
                id = postDetails!!.id,
                categoryId = category.value.toLong(),
                userId = myId,
                title = title.value,
                content = content.value,
                images = selectedImageList.value.map {
                    PostImageEntity(it.toString())
                },
                viewsCount = 0,
                likesCount = 0,
                commentCount = 0,
                bookmarksCount = 0,
                createdAt = postDetails!!.createdAt,
                updatedAt = Clock.System.now()
            )
        ) { isSucceed ->

            callback(isSucceed)
        }
    }

    fun uploadPost(
        myId: UUID,
        callback: (String, Boolean) -> Unit
    ) {
        val success = { msg: String ->
            callback(msg, true)
        }
        val failure = { msg: String ->
            callback(msg, false)
        }
        val postEntity = PostEntity(
            id = 0,
            categoryId = category.value.toLong(),
            userId = myId,
            title = title.value,
            content = content.value,
            images = selectedImageList.value.map {
                PostImageEntity(it.toString())
            },
            viewsCount = 0,
            likesCount = 0,
            commentCount = 0,
            bookmarksCount = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        viewModelScope.launch {
            myInfoUseCase.getMyAccessToken().collect { accessToken ->

                if (accessToken != null) {

                    postUseCase.createPost(postEntity) {

                        if (it) {
                            success("게시글 작성 완료.")
                        } else {
                            failure("게시글 작성 실패. 다시 시도 해주세요.")
                        }
                    }
                } else {
                    success("로그인 해주세요.")
                }
            }
        }
    }
}