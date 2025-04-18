package com.sohae.feature.searchpost.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.domain.post.usecase.PostUseCase
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPostViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    private val postUseCase: PostUseCase
): ViewModel() {

    val recentMySearchHistoryList = myInfoUseCase.getAllMySearchHistoryList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private var _showRecentSearchList = MutableStateFlow(true)
    val showRecentSearchList = _showRecentSearchList.asStateFlow()

    private var _searchPostTitle = MutableStateFlow("")
    val searchPostTitle = _searchPostTitle.asStateFlow()

    private var _requestSearchPostTitle = MutableStateFlow("")
    val requestSearchPostTitle = _requestSearchPostTitle.asStateFlow()

    fun initSearchPostTitle() {
        _searchPostTitle.value = requestSearchPostTitle.value
    }

    fun updateShowRecentSearchList(input: Boolean) {
        _showRecentSearchList.value = input
    }

    fun updateSearchPostTitle(input: String) {
        _searchPostTitle.value = input
    }

    fun searchPostRequest(
        inputSearchPostTitle: String = "",
        onFailure: (String) -> Unit
    ) {
        if ((searchPostTitle.value.isNotBlank() && searchPostTitle.value == requestSearchPostTitle.value)
            || (inputSearchPostTitle.isNotBlank() && inputSearchPostTitle == requestSearchPostTitle.value)) {
            onFailure("현재 검색어와 동일합니다.")
        } else {

            if (inputSearchPostTitle.isNotBlank()) { _searchPostTitle.value = inputSearchPostTitle }

            _requestSearchPostTitle.value = searchPostTitle.value
            _showRecentSearchList.value = false

            viewModelScope.launch {
                myInfoUseCase.updateMySearchHistory(
                    MySearchHistoryEntity(
                        id = 0,
                        keyword = requestSearchPostTitle.value
                    )
                )
            }
        }
    }

    fun deleteMySearchHistory(id: Int = -1) {

        if (id < 0) {
            myInfoUseCase.deleteAllMySearchHistory()
        } else {
            myInfoUseCase.deleteMySearchHistoryById(id)
        }
    }
}