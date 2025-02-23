package com.sohae.navigation.homenavgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sohae.presentation.community.CommunityView
import com.sohae.presentation.home.HomeView
import com.sohae.presentation.home.HomeViewModel
import com.sohae.presentation.houseaccount.HouseAccountView
import com.sohae.presentation.jobnavgraph.JobInfoNavGraphView
import com.sohae.presentation.profile.ProfileView
import com.sohae.presentation.profile.ProfileViewModel
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraphView(
    homeNavController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { HomeBottomNavBar(homeNavController) }
    ) {
        val innerPadding = PaddingValues(
            0.dp,
            0.dp,
            0.dp,
            it.calculateBottomPadding()
        )
        var previousRoute by rememberSaveable {
            mutableStateOf(HomeNavGraphBarItems.HOME)
        }
        val currentRoute = homeNavController.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
            HomeNavGraphBarItems.valueOf(route)
        } ?: HomeNavGraphBarItems.HOME
        var transitionDir by remember { mutableIntStateOf(1) }

        LaunchedEffect(currentRoute) {
            runBlocking {
                (currentRoute.idx - previousRoute.idx).let {
                    transitionDir = if (it > 0) { 1 } else { -1 }
                }

                previousRoute = currentRoute
            }
        }

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = homeNavController,
            startDestination = HomeNavGraphBarItems.HOME.name,
            enterTransition = { slideInHorizontally( initialOffsetX = { transitionDir * it } ) },
            exitTransition = { slideOutHorizontally( targetOffsetX = { (-transitionDir) * it } ) }
        ) {
            composable(HomeNavGraphBarItems.HOME.name) {
                HomeView(hiltViewModel<HomeViewModel>())
            }
            composable(HomeNavGraphBarItems.COMMUNITY.name) {
                CommunityView()
            }
            composable(HomeNavGraphBarItems.JOBSEARCH.name) {
                JobInfoNavGraphView()
            }
            composable(HomeNavGraphBarItems.HOUSEACCOUNT.name) {
                HouseAccountView()
            }
            composable(HomeNavGraphBarItems.PROFILE.name) {
                ProfileView(hiltViewModel<ProfileViewModel>())
            }
        }
    }
}

@Composable
private fun HomeBottomNavBar(
    homeNavController : NavHostController
) {
    val currentBackStackEntry = homeNavController.currentBackStackEntryAsState()

    Column {
        // 광고
        if (false) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                color = MaterialTheme.colorScheme.secondary
            ) {}
        }

        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            elevation = 0.dp
        ) {
            HomeNavGraphItemsList.forEach { item ->
                val isSelected : Boolean = currentBackStackEntry
                    .value?.destination?.route?.let {
                        it == item.route
                    } ?: false

                BottomNavigationItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            homeNavController.navigate(item.route) {
                                homeNavController.popBackStack()
                            }
                        }
                    },
                    icon = {
                        Column {
                            Icon(
                                painter = isSelected.let{
                                    if (it) { painterResource(id = item.seletedIcon) }
                                    else { painterResource(id = item.baseIcon) }
                                },
                                tint = MaterialTheme.colorScheme.primaryContainer,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                        }
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}