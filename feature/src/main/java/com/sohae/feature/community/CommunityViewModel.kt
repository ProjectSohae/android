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

    fun selectCategory(input: CommunityCategory) {

        _selectedCategory.value = input
        _selectedSubCategory.value = if (input != CommunityCategory.HOT) {
            -1
        } else { 0 }
    }

    fun selectSubCategory(input: Int) {
        _selectedSubCategory.value = input
    }
}