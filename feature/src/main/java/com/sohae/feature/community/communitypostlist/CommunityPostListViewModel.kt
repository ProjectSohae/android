package com.sohae.feature.community.communitypostlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.post.usecase.PostUseCase
import com.sohae.feature.community.category.CommunityCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostListViewModel @Inject constructor(
    private val postUseCase: PostUseCase
): ViewModel() {

    var currentPage = 1

    val PAGE_OFFSET = 20

    private var _isReadyPostsList = MutableStateFlow(false)
    val isReadyPostsList = _isReadyPostsList.asStateFlow()

    private var _previewPostsList = MutableStateFlow<List<PostEntity>>(emptyList())
    val previewPostsList = _previewPostsList.asStateFlow()

    private var _maxFirstVisibleItemIndex = MutableStateFlow(-1)
    val maxFirstVisibleItemIndex = _maxFirstVisibleItemIndex.asStateFlow()

    fun setIsReadyPostsList(input: Boolean) {
        _isReadyPostsList.value = input
    }

    fun setMaxFirstVisibleItemIndex(input: Int) {
        _maxFirstVisibleItemIndex.value = input
    }

    fun getPreviewPostsList(
        page: Int,
        category: CommunityCategory,
        subCategoryIdx: Int,
        callback: (String, Boolean) -> Unit
    ) {
        val onSucceed = { getPostList: List<PostEntity> ->
            setIsReadyPostsList(true)
            _previewPostsList.value += getPostList
            callback("", true)
        }
        val onFailure = { msg: String ->
            setIsReadyPostsList(true)
            callback(msg, false)
        }

        viewModelScope.launch {

            when (category) {
                CommunityCategory.ALL -> {
                    postUseCase.getPreviewPostsList(
                        page,
                        subCategoryIdx.toLong()
                    ) {

                        if (it.isNotEmpty()) {
                            onSucceed(it)
                        } else {
                            onFailure("불러올 게시글이 더 이상 없습니다.")
                        }
                    }
                }

                CommunityCategory.HOT -> {
                    postUseCase.getPreviewPopularPostsList(
                        page,
                        subCategoryIdx
                    ) {

                        if (it.isNotEmpty()) {
                            onSucceed(it)
                        } else {
                            onFailure("불러올 게시글이 더 이상 없습니다.")
                        }
                    }
                }

                CommunityCategory.NOTICE -> {
                    postUseCase.getPreviewPostsList(
                        page,
                        (CommunityCategory.NOTICE.idx + 1).toLong()
                    ) {

                        if (it.isNotEmpty()) {
                            onSucceed(it)
                        } else {
                            onFailure("불러올 게시글이 더 이상 없습니다.")
                        }
                    }
                }
            }
        }
    }
}