package com.sohae.feature.community.communitypostlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.community.category.CommunityCategory
import com.sohae.domain.community.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostListViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase
): ViewModel() {

    private var _initPostsList = MutableStateFlow(false)
    val initPostsList = _initPostsList.asStateFlow()

    private var _isReadyPostsList = MutableStateFlow(false)
    val isReadyPostsList = _isReadyPostsList.asStateFlow()

    private var _previewPostsList = MutableStateFlow<List<PostEntity>>(emptyList())
    val previewPostsList = _previewPostsList.asStateFlow()

    fun setInitPostsList(input: Boolean) {
        _initPostsList.value = input
    }

    fun setIsReadyPostsList(input: Boolean) {
        _isReadyPostsList.value = input
    }

    fun getPreviewPostsList(
        page: Int,
        category: CommunityCategory,
        subCategoryIdx: Int,
        callback: (String) -> Unit
    ) {
        Log.d("sohae_", "load")
        val onSucceed = { getPostList: List<PostEntity> ->
            setIsReadyPostsList(true)
            _previewPostsList.value += getPostList
        }
        val onFailure = { msg: String ->
            setInitPostsList(true)
            callback(msg)
        }

        viewModelScope.launch {
            myInfoUseCase.getMyAccessToken().collect { myAccessToken ->

                if (myAccessToken != null) {

                    when (category) {
                        CommunityCategory.ALL -> {
                            postUseCase.getPreviewPostsList(
                                myAccessToken,
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