package com.example.gongik.view.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.gongik.controller.BarColorController
import com.example.gongik.ui.theme.GongikTheme
import com.example.gongik.view.composables.main.MainNavGraphView
import com.example.gongik.view.snackbar.SnackBarBehindTarget
import com.example.gongik.view.snackbar.SnackBarController
import com.example.gongik.view.snackbar.SnackbarView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        setContent {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            GongikTheme {
                val navigationBarColor = BarColorController.navigationBarColor.collectAsState().value

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawRect(
                                color = navigationBarColor,
                                topLeft = Offset.Zero,
                                size = this.size
                            )
                        },
                    snackbarHost = {
                        SnackbarHost(
                            modifier = Modifier.offset(y = (-60).dp),
                            hostState = SnackBarController.snackbarHostState
                        ) { getSnackbarData ->
                            if (SnackBarController.behindTarget == SnackBarBehindTarget.VIEW) {
                                SnackBarController.currentSnackbar = getSnackbarData
                                SnackbarView(snackbarData = getSnackbarData)
                            }
                        }
                    }
                ) {
                    val innerPadding = PaddingValues(
                        it.calculateLeftPadding(LayoutDirection.Ltr),
                        0.dp,
                        it.calculateRightPadding(LayoutDirection.Ltr),
                        it.calculateBottomPadding()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        MainNavGraphView()
                    }
                }
            }
        }
    }
}