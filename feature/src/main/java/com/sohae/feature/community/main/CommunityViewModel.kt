package com.sohae.feature.community.main

import androidx.lifecycle.ViewModel
import com.sohae.feature.community.category.CommunityCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommunityViewModel : ViewModel() {

    private var _selectedCategory = MutableStateFlow(CommunityCategory.ALL)
    val selectedCategory = _selectedCategory.asStateFlow()

    private var _preSelectedSubCategoryIdx = MutableStateFlow(-1)
    val preSelectedSubCategoryIdx = _preSelectedSubCategoryIdx.asStateFlow()

    private var _selectedSubCategoryIdx = MutableStateFlow(-1)
    val selectedSubCategoryIdx = _selectedSubCategoryIdx.asStateFlow()

    fun selectCategory(input: CommunityCategory) {

        _selectedCategory.value = input
        _preSelectedSubCategoryIdx.value = if (input == CommunityCategory.HOT) {
            _selectedSubCategoryIdx.value
        } else { -1 }
        _selectedSubCategoryIdx.value = if (input != CommunityCategory.HOT) {
            -1
        } else { 0 }
    }

    fun updatePreSelectedSubCategoryIdx(input: Int) {
        _preSelectedSubCategoryIdx.value = input
    }

    fun selectSubCategory(input: Int) {
        _selectedSubCategoryIdx.value = input
    }
}