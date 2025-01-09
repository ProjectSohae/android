package com.example.gongik.view.composables.writepost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WritePostViewModel: ViewModel() {
    private var _isTitleBlank = MutableStateFlow(true)
    val isTitleBlank = _isTitleBlank.asStateFlow()

    private var _isContentBlank = MutableStateFlow(true)
    val isContentBlank = _isContentBlank.asStateFlow()

    private var _isCategorySelected = MutableStateFlow(false)
    val isCategorySelected = _isCategorySelected.asStateFlow()

    fun setIsTitleBlank(input: Boolean) {
        _isTitleBlank.value = input
    }

    fun setIsContentBlank(input: Boolean) {
        _isContentBlank.value = input
    }

    fun setIsCategorySelected(input: Boolean) {
        _isCategorySelected.value = input
    }
}