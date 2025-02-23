package com.sohae.navigation.homenavgraph

enum class HomeNavGraphBarItems(
    val idx: Int
) {
    HOME(0),
    COMMUNITY(1),
    JOBSEARCH(2),
    HOUSEACCOUNT(3),
    PROFILE(4)
}

val HomeNavGraphItemsList : List<HomeNavGraphItem> = listOf(
    HomeNavGraphItem.home,
    HomeNavGraphItem.community,
    HomeNavGraphItem.jobSearch,
    HomeNavGraphItem.houseAccount,
    HomeNavGraphItem.profile
)

sealed class HomeNavGraphItem(
    val label : String,
    val route : String,
    val baseIcon : Int,
    val seletedIcon : Int
) {
    object home : HomeNavGraphItem(
        label = "홈",
        route = HomeNavGraphBarItems.HOME.name,
        baseIcon = R.drawable.outline_home_24,
        seletedIcon = R.drawable.baseline_home_24
    )

    object community : HomeNavGraphItem(
        label = "커뮤니티",
        route = HomeNavGraphBarItems.COMMUNITY.name,
        baseIcon = R.drawable.outline_community_24,
        seletedIcon = R.drawable.baseline_community_24
    )

    object houseAccount : HomeNavGraphItem(
        label = "가계부",
        route = HomeNavGraphBarItems.HOUSEACCOUNT.name,
        baseIcon = R.drawable.outline_household_account_24,
        seletedIcon = R.drawable.baseline_household_account_24
    )

    object jobSearch : HomeNavGraphItem(
        label = "복무정보",
        route = HomeNavGraphBarItems.JOBSEARCH.name,
        baseIcon = R.drawable.outline_job_search_24,
        seletedIcon = R.drawable.baseline_job_search_24
    )

    object profile : HomeNavGraphItem(
        label = "프로필",
        route = HomeNavGraphBarItems.PROFILE.name,
        baseIcon = R.drawable.outline_person_24,
        seletedIcon = R.drawable.baseline_person_24
    )
}