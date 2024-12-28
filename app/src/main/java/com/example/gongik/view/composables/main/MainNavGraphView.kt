package com.example.gongik.view.composables.main

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gongik.model.viewmodel.MainNavGraphBarItems
import com.example.gongik.view.composables.home.HomeNavGraphView
import com.example.gongik.view.composables.home.HomeView
import com.example.gongik.view.composables.jobReview.JobReviewView
import com.example.gongik.view.composables.writePost.WritePostView
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

    LaunchedEffect(currentRoute) {

        if (currentRoute.isNotBlank()) {
            mainNavController.navigate(currentRoute)
            MainNavController.navigate("")
        }
    }

    NavHost(
        navController = mainNavController,
        startDestination = MainNavGraphBarItems.HOMENAV.name
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