package com.example.gongik.view.composables.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gongik.model.viewmodel.MainNavGraphBarItems
import com.example.gongik.view.composables.home.HomeNavGraphView
import com.example.gongik.view.composables.jobreview.JobReviewView
import com.example.gongik.view.composables.writepost.WritePostView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MainNavController {
    private var _route : MutableStateFlow<String> = MutableStateFlow("")
    val route : StateFlow<String> = _route.asStateFlow()

    fun navigate(inputRoute : String) {
        _route.value = inputRoute
    }
}

@Composable
fun MainNavGraphView(
    mainNavController : NavHostController = rememberNavController()
) {
    val currentRoute = MainNavController.route.collectAsState().value
    var transitionDir by remember { mutableIntStateOf(1) }

    LaunchedEffect(currentRoute) {

        if (currentRoute.isNotBlank()) {
            mainNavController.navigate(currentRoute)
            MainNavController.navigate("")
        }
    }

    Log.d("test", "${transitionDir}")

    BackHandler {
        transitionDir = -1
        mainNavController.popBackStack()
        transitionDir = 1
    }

    NavHost(
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