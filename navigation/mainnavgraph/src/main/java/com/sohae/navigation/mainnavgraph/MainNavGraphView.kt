package com.sohae.navigation.mainnavgraph

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sohae.controller.mainnavgraph.MainNavGraphRoutes
import com.sohae.controller.mainnavgraph.MainNavGraphViewController
import com.sohae.controller.mainnavgraph.MainScreenController
import com.sohae.navigation.homenavgraph.HomeNavGraphView
import com.sohae.presentation.inquiryemail.InquiryEmailView
import com.sohae.presentation.jobinformation.JobInformationView
import com.sohae.presentation.jobreview.JobReviewView
import com.sohae.presentation.mycommentlist.MyCommentListView
import com.sohae.presentation.mypostlist.MyPostListView
import com.sohae.presentation.myprofile.MyProfileView
import com.sohae.presentation.post.PostImageView
import com.sohae.presentation.post.PostView
import com.sohae.presentation.searchjob.SearchJobView
import com.sohae.presentation.searchpost.SearchPostView
import com.sohae.presentation.searchpost.SearchPostViewModel
import com.sohae.presentation.selectimage.SelectImageView
import com.sohae.presentation.selectimage.SelectImageViewModel
import com.sohae.presentation.settingoptions.SettingOptionsView
import com.sohae.presentation.writejobreview.WriteJobReviewView
import com.sohae.presentation.writepost.WritePostView
import com.sohae.presentation.writepost.WritePostViewModel
import dev.chrisbanes.haze.hazeSource

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainNavGraphView(
    mainNavGraphViewModel: MainNavGraphViewModel = viewModel()
) {
    MainNavGraphViewController.mainNavController = rememberNavController()

    val mainNavController = MainNavGraphViewController.mainNavController
    val hazeState by remember { mutableStateOf(MainScreenController.hazeState) }

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
            composable(MainNavGraphRoutes.SELECTIMAGE.name) {
                val selectImageViewModel: SelectImageViewModel = viewModel()

                mainNavController.previousBackStackEntry?.savedStateHandle
                    ?.get<List<Uri>>("selected_image_list")?.let {
                        selectImageViewModel.initSelectedImageList(it)
                    }

                SelectImageView(
                    selectImageViewModel
                ) { selectedImageList ->
                    mainNavController.previousBackStackEntry?.savedStateHandle?.set("selected_image_list", selectedImageList)
                    mainNavController.popBackStack()
                }
            }
            // in community view
            composable(MainNavGraphRoutes.WRITEPOST.name) {
                val writePostViewModel: WritePostViewModel = viewModel()

                writePostViewModel.setSelectedImageList(
                    mainNavController.currentBackStackEntry?.savedStateHandle
                        ?.get<List<Uri>>("selected_image_list") ?: emptyList()
                )

                WritePostView(
                    writePostViewModel = writePostViewModel
                )
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
            composable(MainNavGraphRoutes.MYJOBREVIEWLIST.name) {

            }
            composable(MainNavGraphRoutes.INQUIRYEMAIL.name) {
                InquiryEmailView()
            }
        }
    }
}