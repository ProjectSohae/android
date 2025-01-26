package com.example.gongik.view.main

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.gongik.view.home.HomeNavGraphView
import com.example.gongik.view.jobreview.JobReviewView
import com.example.gongik.view.post.PostView
import com.example.gongik.view.searchjob.SearchJobView
import com.example.gongik.view.searchpost.SearchPostView
import com.example.gongik.view.writejobreview.WriteJobReviewView
import com.example.gongik.view.writepost.WritePostView
import dev.chrisbanes.haze.haze

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraphView(
    mainNavController : NavHostController = rememberNavController()
) {
    val currentRoute = MainNavGraphViewModel.route.collectAsState().value
    val isBackPressed = MainNavGraphViewModel.backPressed.collectAsState().value
    val isDeactive = MainNavGraphViewModel.isDeactive.collectAsState().value
    val getParam = MainNavGraphViewModel.param.collectAsState().value
    val transitionDir = 1
    val hazeState by remember { mutableStateOf(MainNavGraphViewModel.hazeState) }

    LaunchedEffect(isBackPressed) {

        if (isBackPressed) {
            mainNavController.popBackStack()
            MainNavGraphViewModel.finishPopBack()
        }
    }

    LaunchedEffect(getParam) {

        if (getParam.first.isNotBlank()) {
            mainNavController.currentBackStackEntry?.savedStateHandle?.set(getParam.first, getParam.second)
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
            startDestination = MainNavGraphItems.HOMENAV.name,
            enterTransition = { slideInVertically { (-transitionDir) * it } },
            exitTransition = { slideOutVertically { transitionDir * it } }
        ) {
            composable(MainNavGraphItems.HOMENAV.name) {
                HomeNavGraphView()
            }
            composable(MainNavGraphItems.JOBREVIEW.name) {
                JobReviewView()
            }
            composable(MainNavGraphItems.WRITEJOBREVIEW.name) {
                WriteJobReviewView()
            }
            composable(MainNavGraphItems.WRITEPOST.name) {
                WritePostView()
            }
            composable(MainNavGraphItems.POST.name) {
                val pressedPostUid by remember {
                    mutableStateOf(mainNavController.previousBackStackEntry?.savedStateHandle?.get<Int>("pressed_post_uid"))
                }

                PostView(pressedPostUid)
            }
            composable(MainNavGraphItems.SEARCHPOST.name) {
                SearchPostView()
            }
            composable(MainNavGraphItems.SEARCHJOB.name) {
                SearchJobView()
            }
            composable(MainNavGraphItems.SETTING.name) {

            }
        }

    }
}