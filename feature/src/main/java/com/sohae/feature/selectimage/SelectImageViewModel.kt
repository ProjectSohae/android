package com.sohae.feature.selectimage

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.allowHardware
import coil3.request.allowRgb565
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectImageViewModel: ViewModel() {

    lateinit var imageLoader: ImageLoader

    private var _isReadyViewModel = MutableStateFlow(false)
    val isReadyViewModel = _isReadyViewModel.asStateFlow()

    private var initialSelectedImageList: List<Uri> = emptyList()

    private val _selectedImagesList = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImagesList = _selectedImagesList.asStateFlow()

    private val _isSelectedImageListChanged = MutableStateFlow(false)
    val isSelectedImageListChanged = _isSelectedImageListChanged.asStateFlow()

    fun getAllImages(
        context: Context,
        callBack: (List<Uri>) -> Unit
    ) {
        var cnt: Int = 0
        val imageList = mutableListOf<Uri>()

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

                Log.d("checkD", "${cursor.count}")

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(collection, id)

                    imageList.add(uri)
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