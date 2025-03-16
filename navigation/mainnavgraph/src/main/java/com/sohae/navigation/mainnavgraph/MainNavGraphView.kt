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
import com.sohae.feature.jobinformation.JobInformationView
import com.sohae.feature.jobreview.JobReviewView
import com.sohae.feature.mycommentlist.MyCommentListView
import com.sohae.feature.mypostlist.MyPostListView
import com.sohae.feature.myprofile.MyProfileView
import com.sohae.feature.myprofile.MyProfileViewModel
import com.sohae.feature.post.post.PostImageView
import com.sohae.feature.post.post.PostView
import com.sohae.feature.post.post.PostViewModel
import com.sohae.feature.searchjob.SearchJobView
import com.sohae.feature.searchpost.SearchPostView
import com.sohae.feature.searchpost.SearchPostViewModel
import com.sohae.feature.selectimage.SelectImageView
import com.sohae.feature.selectimage.SelectImageViewModel
import com.sohae.feature.settingoptions.inquiryemail.InquiryEmailView
import com.sohae.feature.settingoptions.main.SettingOptionsView
import com.sohae.feature.writejobreview.WriteJobReviewView
import com.sohae.feature.writepost.WritePostView
import com.sohae.feature.writepost.WritePostViewModel
import com.sohae.navigation.homenavgraph.HomeNavGraphView
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
                val selectImageViewModel = viewModel<SelectImageViewModel>()

                mainNavController.previousBackStackEntry?.savedStateHandle
                    ?.get<List<Uri>>("selected_image_list")?.let {
                        selectImageViewModel.initSelectedImageList(it)
                    }

                SelectImageView(
                    selectImageViewModel
                ) { selectedImageList ->
                    mainNavController.previousBackStackEntry?.savedStateHandle?.set(
                        "selected_image_list",
                        selectedImageList
                    )
                    mainNavController.popBackStack()
                }
            }
            // in community view
            composable(MainNavGraphRoutes.WRITEPOST.name) {
                val writePostViewModel = hiltViewModel<WritePostViewModel>()

                writePostViewModel.postId = mainNavController
                    .previousBackStackEntry?.savedStateHandle?.get("modify_post_id")

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
                    mutableStateOf(mainNavController.previousBackStackEntry?.savedStateHandle?.get<Long>("selected_post_id"))
                }

                PostView(
                    pressedPostId,
                    hiltViewModel<PostViewModel>()
                )
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
                MyProfileView(hiltViewModel<MyProfileViewModel>())
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