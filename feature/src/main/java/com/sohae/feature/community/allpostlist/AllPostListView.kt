package com.sohae.feature.community.allpostlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sohae.domain.community.category.CommunityCategory
import com.sohae.feature.community.communitypostlist.CommunityPostListView
import com.sohae.feature.community.main.CommunityViewModel
import com.sohae.feature.community.communitypostlist.CommunityPostListViewModel

@Composable
fun AllPostNavView(
    communityViewModel: CommunityViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentSelectedSubCategoryIdx = communityViewModel.selectedSubCategory.collectAsState().value

    CommunityPostListView(
        currentSelectedCategory = CommunityCategory.ALL,
        currentSelectedSubCategoryIdx = currentSelectedSubCategoryIdx,
        communityPostListViewModel = hiltViewModel<CommunityPostListViewModel>()
    )
}