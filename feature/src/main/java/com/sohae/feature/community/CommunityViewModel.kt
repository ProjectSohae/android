package com.sohae.feature.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.community.category.CommunityCategory
import com.sohae.domain.community.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase
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
        subCategoryIdx: Int,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            myInfoUseCase.getMyAccessToken().collect { myAccessToken ->

                if (myAccessToken != null) {

                    when (category) {
                        CommunityCategory.ALL -> {
                            postUseCase.getPreviewPostsList(
                                myAccessToken,
                                1,
                                subCategoryIdx.toLong()
                            ) {

                                if (it.isNotEmpty()) {
                                    _previewPostsList.value += it
                                } else {
                                    onFailure("불러올 게시글이 더 이상 없습니다.")
                                }
                            }
                        }

                        CommunityCategory.HOT -> {

                        }

                        CommunityCategory.NOTICE -> {

                        }

                        else -> {
                            onFailure("잘못된 카테고리 입니다.")
                        }
                    }
                } else {
                    onFailure("로그인 상태가 아닙니다.")
                }
            }
        }
    }
}