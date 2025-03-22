package com.sohae.feature.mypostlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.post.usecase.PostUseCase
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import com.sohae.feature.community.category.CommunityCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostListViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase
): ViewModel() {

    val communityCategory = CommunityCategory.ALL.subCategories

    private var _myPostsList = MutableStateFlow<List<PostEntity>>(emptyList())
    val myPostsList = _myPostsList.asStateFlow()

    fun getMyPostsList(
        page: Int,
        callback: (String, Boolean) -> Unit
    ) {
        val failure = { msg: String ->
            callback(msg, false)
        }

        viewModelScope.launch {
            myInfoUseCase.getMyAccount().collect { getMyAccount ->
                getMyAccount.let { myAccount ->

                    if (myAccount != null) {
                        postUseCase.getPreviewPostsListByUserId(page, myAccount.id) { getMyPostsList ->

                            if (getMyPostsList.isEmpty()) {
                                failure("불러올 게시글이 더 이상 없습니다.")
                            } else {
                                _myPostsList.value += getMyPostsList
                            }
                        }
                    } else {
                        failure("로그인이 필요합니다.")
                    }
                }
            }
        }
    }
}