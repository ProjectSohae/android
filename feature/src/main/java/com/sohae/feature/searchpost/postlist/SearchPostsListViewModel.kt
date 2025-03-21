package com.sohae.feature.searchpost.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.community.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPostsListViewModel @Inject constructor(
    private val postUseCase: PostUseCase
): ViewModel() {

    private var page = 1

    private var _isReadyPostsList = MutableStateFlow(false)
    val isReadyPostsList = _isReadyPostsList.asStateFlow()

    private var _previewPostsList = MutableStateFlow<List<PostEntity>>(emptyList())
    val previewPostsList = _previewPostsList.asStateFlow()

    fun getPreviewPostsListByKeyword(
        keyword: String,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            postUseCase.getPreviewPostsListByKeyword(page, keyword) { getPreviewPostsList ->

                if (getPreviewPostsList.isNotEmpty()) {
                    _previewPostsList.value += getPreviewPostsList
                    page++
                }

                if (!isReadyPostsList.value) { _isReadyPostsList.value = true }
            }
        }
    }
}