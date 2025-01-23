package com.example.gongik.view.composables.searchjob

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchJobViewModel: ViewModel() {

    private var _searchJobName = MutableStateFlow("")
    val searchJobName = _searchJobName.asStateFlow()

    private var _searchJobList = MutableStateFlow<List<String>>(emptyList())
    val searchJobList = _searchJobList.asStateFlow()

    fun updateSearchJobName(input: String) {
        _searchJobName.value = input

        if (_searchJobName.value.isNotBlank()) {
            _searchJobList.value = listOf(
                "서울교통공사","서울교통공사","서울교통공사","서울교통공사",
            ).filter { it.contains(_searchJobName.value) }
        }
    }
}