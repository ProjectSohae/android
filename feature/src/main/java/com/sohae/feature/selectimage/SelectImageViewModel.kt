package com.sohae.feature.selectimage

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectImageViewModel: ViewModel() {

    private var _isReadyViewModel = MutableStateFlow(false)
    val isReadyViewModel = _isReadyViewModel.asStateFlow()

    private var initialSelectedImageList: List<Uri> = emptyList()

    private val _selectedImagesList = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImagesList = _selectedImagesList.asStateFlow()

    private val _isSelectedImageListChanged = MutableStateFlow(false)
    val isSelectedImageListChanged = _isSelectedImageListChanged.asStateFlow()

    fun getAllImages(
        context: Context,
        callBack: (List<List<Uri>>) -> Unit
    ) {
        var rowIdx = 0
        val imageList = mutableListOf<List<Uri>>(emptyList())

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        viewModelScope.launch {
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(collection, id)

                    if (imageList[rowIdx].size >= 3) {
                        rowIdx++
                        imageList.add(emptyList())
                    }

                    imageList[rowIdx] = imageList[rowIdx] + uri
                }

                callBack(imageList)
            }
        }
    }

    fun initSelectedImageList(input: List<Uri>) {
        initialSelectedImageList = input
        _selectedImagesList.value = input
    }

    fun selectImage(input: Uri): String {
        val exceedSize = "사진은 10개 까지만 선택할 수 있습니다."

        if (selectedImagesList.value.contains(input)) {
            _selectedImagesList.value -= input
        } else {

            if (selectedImagesList.value.size < 10) {
                _selectedImagesList.value += input
            } else {
                return exceedSize
            }
        }

        _isSelectedImageListChanged.value =
            (selectedImagesList.value != initialSelectedImageList)

        return ""
    }
}