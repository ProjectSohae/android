package com.sohae.feature.community.category

import kotlinx.serialization.Serializable

@Serializable
sealed class CommunityCategoryRoute {

    @Serializable
    data object ALL: CommunityCategoryRoute() {

        @Serializable
        object MAIN

        @Serializable
        object COMMON

        @Serializable
        object QUESTION

        @Serializable
        object TIP
    }

    @Serializable
    data object HOT: CommunityCategoryRoute() {

        @Serializable
        object DAY

        @Serializable
        object WEEK

        @Serializable
        object MONTH
    }

    @Serializable
    data object NOTICE: CommunityCategoryRoute()
}