package com.sohae.feature.writepost

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.post.entity.PostImageEntity
import com.sohae.domain.community.usecase.PostUseCase
import com.sohae.feature.community.CommunityCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
    private val postUseCase: com.sohae.domain.community.usecase.PostUseCase
): ViewModel() {

    val categoryList = CommunityCategory.ALL.subCategories

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
        callBack: (String, Boolean) -> Unit
    ) {
        val postEntity = PostEntity(
            0,
            category.value.toLong(),
            UUID.randomUUID(),
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

        postUseCase.createPost(postEntity) {

            if (it) {
                callBack("게시글 작성 완료.", it)
            } else {
                callBack("게시글 작성 실패. 다시 시도 해주세요.", it)
            }
        }
    }
}