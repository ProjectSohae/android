package com.jhw.sohae.navigation.mainnavgraph

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jhw.sohae.controller.mainnavgraph.MainNavController
import com.jhw.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.jhw.sohae.controller.mainnavgraph.MainScreenController
import com.jhw.sohae.navigation.homenavgraph.HomeNavGraphView
import com.jhw.sohae.presentation.jobinformation.JobInformationView
import com.jhw.sohae.presentation.jobreview.JobReviewView
import com.jhw.sohae.presentation.mycommentlist.MyCommentListView
import com.jhw.sohae.presentation.mypostlist.MyPostListView
import com.jhw.sohae.presentation.myprofile.MyProfileView
import com.jhw.sohae.presentation.post.PostImageView
import com.jhw.sohae.presentation.post.PostView
import com.jhw.sohae.presentation.searchjob.SearchJobView
import com.jhw.sohae.presentation.searchpost.SearchPostView
import com.jhw.sohae.presentation.searchpost.SearchPostViewModel
import com.jhw.sohae.presentation.settingoptions.SettingOptionsView
import com.jhw.sohae.presentation.writejobreview.WriteJobReviewView
import com.jhw.sohae.presentation.writepost.WritePostView
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeSource

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainNavGraphView(
    mainNavGraphViewModel: MainNavGraphViewModel = viewModel(),
    mainNavController : NavHostController = rememberNavController()
) {
    val currentRoute = MainNavController.route.collectAsState().value
    val isBackPressed = MainNavController.backPressed.collectAsState().value
    val isDeactive = MainScreenController.isDeactive.collectAsState().value
    val getParam = MainNavController.param.collectAsState().value
    val transitionDir = 1
    val hazeState by remember { mutableStateOf(MainScreenController.hazeState) }

    LaunchedEffect(isBackPressed) {

        if (isBackPressed) {
            mainNavController.popBackStack()
            MainNavController.finishPopBack()
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
            MainNavController.navigate("")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NavHost(
            modifier = Modifier.hazeSource(hazeState),
            navController = mainNavController,
            startDestination = MainNavGraphRoutes.HOMENAV.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(MainNavGraphRoutes.HOMENAV.name) {
                HomeNavGraphView()
            }
            // in community view
            composable(MainNavGraphRoutes.WRITEPOST.name) {
                WritePostView()
            }
            composable(MainNavGraphRoutes.POST.name) {
                val pressedPostId by remember {
                    mutableStateOf(mainNavController.previousBackStackEntry?.savedStateHandle?.get<Int>("pressed_post_id"))
                }

                PostView(pressedPostId)
            }
            composable(MainNavGraphRoutes.POSTIMAGE.name) {
                PostImageView("")
            }
            composable(MainNavGraphRoutes.SEARCHPOST.name) {
                SearchPostView(hiltViewModel<SearchPostViewModel>())
            }
            // in job information view
            composable(MainNavGraphRoutes.WRITEJOBREVIEW.name) {
                WriteJobReviewView()
            }
            composable(MainNavGraphRoutes.JOBINFORMATION.name) {
                JobInformationView()
            }
            composable(MainNavGraphRoutes.JOBREVIEW.name) {
                val pressedJobReviewId by remember {
                    mutableStateOf(mainNavController.previousBackStackEntry?.savedStateHandle?.get<Int>("pressed_job_review_id"))
                }

                JobReviewView(pressedJobReviewId)
            }
            composable(MainNavGraphRoutes.SEARCHJOB.name) {
                SearchJobView()
            }
            // in profile view
            composable(MainNavGraphRoutes.MYPROFILE.name) {
                MyProfileView()
            }
            composable(MainNavGraphRoutes.SETTINGOPTIONS.name) {
                SettingOptionsView()
            }
            composable(MainNavGraphRoutes.MYPOSTLIST.name) {
                MyPostListView()
            }
            composable(MainNavGraphRoutes.MYCOMMENTLIST.name) {
                MyCommentListView()
            }
        }
    }
}