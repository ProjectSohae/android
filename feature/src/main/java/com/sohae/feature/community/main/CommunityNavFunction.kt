package com.sohae.feature.community.main

import androidx.navigation.NavHostController
import com.sohae.feature.community.category.CommunityCategory
import com.sohae.feature.community.category.CommunityCategoryNavRoute

fun NavHostController.communityNavigate(
    category: CommunityCategory,
    subCategoryIdx: Int,
    doBeforeNavigate: () -> Unit
) {
    when (category) {
        CommunityCategory.ALL -> {
            when (subCategoryIdx) {
                -1 -> {
                    navigate(CommunityCategoryNavRoute.ALL.MAIN) {
                        doBeforeNavigate()
                    }
                }
                0 -> {
                    navigate(CommunityCategoryNavRoute.ALL.COMMON) {
                        doBeforeNavigate()
                    }
                }
                1 -> {
                    navigate(CommunityCategoryNavRoute.ALL.QUESTION) {
                        doBeforeNavigate()
                    }
                }
                2 -> {
                    navigate(CommunityCategoryNavRoute.ALL.TIP) {
                        doBeforeNavigate()
                    }
                }
                else -> {
                    navigate(CommunityCategoryNavRoute.ALL.MAIN) {
                        doBeforeNavigate()
                    }
                }
            }
        }
        CommunityCategory.HOT -> {
            when (subCategoryIdx) {
                0 -> {
                    navigate(CommunityCategoryNavRoute.HOT.DAY) {
                        doBeforeNavigate()
                    }
                }
                1 -> {
                    navigate(CommunityCategoryNavRoute.HOT.WEEK) {
                        doBeforeNavigate()
                    }
                }
                2 -> {
                    navigate(CommunityCategoryNavRoute.HOT.MONTH) {
                        doBeforeNavigate()
                    }
                }
                else -> {
                    navigate(CommunityCategoryNavRoute.HOT.DAY) {
                        doBeforeNavigate()
                    }
                }
            }
        }
        CommunityCategory.NOTICE -> {
            navigate(CommunityCategoryNavRoute.NOTICE) {
                doBeforeNavigate()
            }
        }
    }
}