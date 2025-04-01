package com.sohae.controller.ui.snackbar

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class SnackBarBehindTarget {
    VIEW,
    DIALOG
}

object SnackBarController{
    val snackbarHostState = SnackbarHostState()
    var currentSnackbar : SnackbarData? = null
    var message : String = ""
    var behindTarget = SnackBarBehindTarget.VIEW

    fun show(
        inputMessage : String,
        inputBehindTarget: SnackBarBehindTarget,
        inputDuration: SnackbarDuration = SnackbarDuration.Short
    ) {
        hide()
        behindTarget = inputBehindTarget
        message = inputMessage
        CoroutineScope(Dispatchers.Default).launch {
            // showSnackbar를 통해 snackbar data가 생성되고, snackbarHostState에 담긴다.
            snackbarHostState.showSnackbar(message, duration = inputDuration)
        }
    }

    fun hide() {
        currentSnackbar?.dismiss()
        currentSnackbar = null
    }
}