package com.example.gongik.view.composables.main

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gongik.util.font.dpToSp
import com.example.gongik.view.composables.home.HomeNavGraphView
import com.example.gongik.view.composables.jobreview.JobReviewView
import com.example.gongik.view.composables.writepost.WritePostView
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MainNavController {
    private var _route = MutableStateFlow("")
    val route : StateFlow<String> = _route.asStateFlow()

    private var _isDeactive = MutableStateFlow(false)
    val isDeactive : StateFlow<Boolean> = _isDeactive.asStateFlow()

    val hazeState = HazeState()

    fun navigate(inputRoute : String) {
        _route.value = inputRoute
    }

    fun deactive() {
        _isDeactive.value = true
    }

    fun active() {
        _isDeactive.value = false
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MainNavGraphView(
    mainNavController : NavHostController = rememberNavController()
) {
    val currentRoute = MainNavController.route.collectAsState().value
    val isDeactive = MainNavController.isDeactive.collectAsState().value
    val blurValue by animateIntAsState(targetValue =
        if (isDeactive) { 5 }
        else { 0 }
    )
    val transitionDir = 1
    val hazeState by remember {
        mutableStateOf(MainNavController.hazeState)
    }

    LaunchedEffect(currentRoute) {

        if (currentRoute.isNotBlank()) {
            mainNavController.navigate(currentRoute)
            MainNavController.navigate("")
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