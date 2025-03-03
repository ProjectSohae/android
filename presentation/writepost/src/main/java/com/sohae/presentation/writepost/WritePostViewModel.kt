package com.sohae.presentation.writepost

import android.net.Uri
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

    private var _selectedImageList = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageList = _selectedImageList.asStateFlow()

    fun setIsTitleBlank(input: Boolean) {
        _isTitleBlank.value = input
    }

    fun setIsContentBlank(input: Boolean) {
        _isContentBlank.value = input
    }

    fun setIsCategorySelected(input: Boolean) {
        _isCategorySelected.value = input
    }

    fun setSelectedImageList(input: List<Uri>) {
        _selectedImageList.value = input
    }
}