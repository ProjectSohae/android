package com.jhw.sohae.controller.barcolor

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object BarColorController {
    private val _statusBarColor = MutableStateFlow(Color.Black)
    val statusBarColor = _statusBarColor.asStateFlow()

    private val _navigationBarColor = MutableStateFlow(Color.Black)
    val navigationBarColor = _navigationBarColor.asStateFlow()

    fun setStatusBarColor(input : Color) {
        _statusBarColor.value = input
    }

    fun setNavigationBarColor(input : Color) {
        _navigationBarColor.value = input
    }
}