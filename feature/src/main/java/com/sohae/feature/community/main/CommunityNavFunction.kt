package com.sohae.feature.community.main

import androidx.navigation.NavHostController
import com.sohae.feature.community.category.CommunityCategory
import com.sohae.feature.community.category.CommunityCategoryRoute

fun NavHostController.communityNavigate(
    category: CommunityCategory,
    subCategoryIdx: Int,
    doBeforeNavigate: () -> Unit
) {
    when (category) {
        CommunityCategory.ALL -> {
            when (subCategoryIdx) {
                -1 -> {
                    navigate(CommunityCategoryRoute.ALL.MAIN) {
                        doBeforeNavigate()
                    }
                }
                0 -> {
                    navigate(CommunityCategoryRoute.ALL.COMMON) {
                        doBeforeNavigate()
                    }
                }
                1 -> {
                    navigate(CommunityCategoryRoute.ALL.QUESTION) {
                        doBeforeNavigate()
                    }
                }
                2 -> {
                    navigate(CommunityCategoryRoute.ALL.TIP) {
                        doBeforeNavigate()
                    }
                }
                else -> {
                    navigate(CommunityCategoryRoute.ALL.MAIN) {
                        doBeforeNavigate()
                    }
                }
            }
        }
        CommunityCategory.HOT -> {
            when (subCategoryIdx) {
                0 -> {
                    navigate(CommunityCategoryRoute.HOT.DAY) {
                        doBeforeNavigate()
                    }
                }
                1 -> {
                    navigate(CommunityCategoryRoute.HOT.WEEK) {
                        doBeforeNavigate()
                    }
                }
                2 -> {
                    navigate(CommunityCategoryRoute.HOT.MONTH) {
                        doBeforeNavigate()
                    }
                }
                else -> {
                    navigate(CommunityCategoryRoute.HOT.DAY) {
                        doBeforeNavigate()
                    }
                }
            }
        }
        CommunityCategory.NOTICE -> {
            navigate(CommunityCategoryRoute.NOTICE) {
                doBeforeNavigate()
            }
        }
    }
}