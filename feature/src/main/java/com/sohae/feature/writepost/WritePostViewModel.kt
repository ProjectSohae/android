package com.sohae.feature.writepost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.post.entity.PostImageEntity
import com.sohae.domain.community.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.sohae.feature.community.CommunityCategory
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
            0,
            category.value.toLong(),
            myId,
            title.value,
            content.value,
            selectedImageList.value.map {
                PostImageEntity(it.toString())
            },
            0,
            0,
            0,
            0,
            Clock.System.now()
        )

        viewModelScope.launch {
            myInfoUseCase.getMyAccessToken().collect { accessToken ->

                if (accessToken != null) {

                    postUseCase.createPost(accessToken, postEntity) {

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