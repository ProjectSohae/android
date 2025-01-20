package com.example.gongik.view.composables.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.gongik.util.font.dpToSp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

@Composable
fun SnackbarView(
    snackbarData: SnackbarData
) {
    var pressedSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(pressedSnackbar) {

        if (pressedSnackbar) {
            SnackBarController.hide()
        }
    }

    Text(
        text = snackbarData.visuals.message,
        fontSize = dpToSp(dp = 16.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(50)
            )
            .clickable { pressedSnackbar = true }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    )
}