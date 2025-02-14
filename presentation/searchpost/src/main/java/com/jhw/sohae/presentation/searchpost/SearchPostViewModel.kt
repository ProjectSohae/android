package com.jhw.sohae.presentation.searchpost

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchPostViewModel: ViewModel() {

    private var _showRecentSearchList = MutableStateFlow(true)
    val showRecentSearchList = _showRecentSearchList.asStateFlow()

    private var _searchPostTitle = MutableStateFlow("")
    val searchPostTitle = _searchPostTitle.asStateFlow()

    private var requestSearchPostTitle = ""

    fun getRequestSearchPostTitle(): String = requestSearchPostTitle

    fun updateShowRecentSearchList(input: Boolean) {
        _showRecentSearchList.value = input
    }

    fun updateSearchPostTitle(input: String) {
        _searchPostTitle.value = input
    }

    fun request(
        inputSearchPostTitle: String = "",
        onFailure: (String) -> Unit
    ) {
        if ((searchPostTitle.value.isNotBlank() && searchPostTitle.value == requestSearchPostTitle)
            || (inputSearchPostTitle.isNotBlank() && inputSearchPostTitle == requestSearchPostTitle)) {
            onFailure("현재 검색어와 동일합니다.")
        } else {

            if (inputSearchPostTitle.isNotBlank()) { _searchPostTitle.value = inputSearchPostTitle }

            requestSearchPostTitle = searchPostTitle.value
            _showRecentSearchList.value = false
        }
    }
}