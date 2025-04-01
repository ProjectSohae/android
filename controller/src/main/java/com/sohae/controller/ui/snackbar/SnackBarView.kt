package com.sohae.controller.ui.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        fontSize = 16.sp,
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