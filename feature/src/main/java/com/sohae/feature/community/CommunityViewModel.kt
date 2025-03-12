package com.sohae.feature.community

import androidx.lifecycle.ViewModel
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.community.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val postUseCase: com.sohae.domain.community.usecase.PostUseCase
) : ViewModel() {

    private var _selectedCategory = MutableStateFlow(CommunityCategory.ALL)
    val selectedCategory = _selectedCategory.asStateFlow()

    private var _selectedSubCategory = MutableStateFlow(-1)
    val selectedSubCategory = _selectedSubCategory.asStateFlow()

    private var _previewPostsList = MutableStateFlow<List<PostEntity>>(emptyList())
    val previewPostsList = _previewPostsList.asStateFlow()

    fun selectCategory(input: CommunityCategory) {

        _selectedCategory.value = input
        _selectedSubCategory.value = if (input != CommunityCategory.HOT) {
            -1
        } else { 0 }
    }

    fun selectSubCategory(input: Int) {
        _selectedSubCategory.value = input
    }

    fun getPreviewPostsList(
        page: Int,
        category: CommunityCategory,
        onFailure: (String) -> Unit
    ) {
        postUseCase.getPreviewPostsList(page + 1, category.idx.toLong()) {

            if (it.isNotEmpty()) {
                _previewPostsList.value += it
            } else {
                onFailure("불러올 게시글이 더 이상 없습니다.")
            }
        }
    }
}