package com.sohae.feature.community.category

import kotlinx.serialization.Serializable

@Serializable
sealed class CommunityCategoryNavRoute {

    @Serializable
    data object ALL: CommunityCategoryNavRoute() {

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
    data object HOT: CommunityCategoryNavRoute() {

        @Serializable
        object DAY

        @Serializable
        object WEEK

        @Serializable
        object MONTH
    }

    @Serializable
    data object NOTICE: CommunityCategoryNavRoute()
}