package com.example.gongik.view.composables.main

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gongik.view.composables.home.HomeNavGraphView
import com.example.gongik.view.composables.jobreview.JobReviewView
import com.example.gongik.view.composables.writejobreview.WriteJobReviewView
import com.example.gongik.view.composables.writepost.WritePostView
import dev.chrisbanes.haze.haze

@Composable
fun MainNavGraphView(
    mainNavController : NavHostController = rememberNavController()
) {
    val currentRoute = MainNavGraphViewModel.route.collectAsState().value
    val isBackPressed = MainNavGraphViewModel.backPressed.collectAsState().value
    val isDeactive = MainNavGraphViewModel.isDeactive.collectAsState().value
    val transitionDir = 1
    val hazeState by remember { mutableStateOf(MainNavGraphViewModel.hazeState) }

    LaunchedEffect(isBackPressed) {

        if (isBackPressed) {
            mainNavController.popBackStack()
            MainNavGraphViewModel.finishPopBack()
        }
    }

    LaunchedEffect(currentRoute) {

        if (currentRoute.isNotBlank()) {
            mainNavController.navigate(currentRoute)
            MainNavGraphViewModel.navigate("")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NavHost(
            modifier = Modifier.haze(hazeState),
            navController = mainNavController,
            startDestination = MainNavGraphBarItems.HOMENAV.name,
            enterTransition = { slideInVertically { (-transitionDir) * it } },
            exitTransition = { slideOutVertically { transitionDir * it } }
        ) {
            composable(MainNavGraphBarItems.HOMENAV.name) {
                HomeNavGraphView()
            }
            composable(MainNavGraphBarItems.JOBREVIEW.name) {
                JobReviewView()
            }
            composable(MainNavGraphBarItems.WRITEJOBREVIEW.name) {
                WriteJobReviewView()
            }
            composable(MainNavGraphBarItems.WRITEPOST.name) {
                WritePostView()
            }
            composable(MainNavGraphBarItems.FINDPOST.name) {

            }
            composable(MainNavGraphBarItems.FINDJOB.name) {

            }
            composable(MainNavGraphBarItems.SETTING.name) {

            }
        }

    }
}