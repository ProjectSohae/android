package com.example.gongik.view.composables.searchjob

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchJobViewModel: ViewModel() {

    private var _searchJobName = MutableStateFlow("")
    val searchJobName = _searchJobName.asStateFlow()

    fun updateSearchJobName(input: String) {
        _searchJobName.value = input
    }
}