package com.sohae.feature.community.hotpostlist

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
fun HotPostNavView(
    communityViewModel: CommunityViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentSelectedSubCategory = communityViewModel.selectedSubCategory.collectAsState().value

    CommunityPostListView(
        currentSelectedCategory = CommunityCategory.HOT,
        currentSelectedSubCategoryIdx = currentSelectedSubCategory,
        communityPostListViewModel = hiltViewModel<CommunityPostListViewModel>()
    )
}