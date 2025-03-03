package com.sohae.presentation.selectimage

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectImageViewModel: ViewModel() {

    private var initialSelectedImageList: List<Uri> = emptyList()

    private val _selectedImagesList = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImagesList = _selectedImagesList.asStateFlow()

    private val _isSelectedImageListChanged = MutableStateFlow(false)
    val isSelectedImageListChanged = _isSelectedImageListChanged.asStateFlow()

    fun getAllImages(context: Context): List<Uri> {
        val imageList = mutableListOf<Uri>()

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

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

                imageList.add(uri)
            }
        }

        return imageList
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