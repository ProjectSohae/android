package com.sohae.domain.myinformation.entity

data class MyRankEntity (

    val id: Int,

    // 일등병 진급 일자
    val firstPromotionDay: Long,

    // 상등병 진급 일자
    val secondPromotionDay: Long,

    // 병장 진급 일자
    val thirdPromotionDay: Long,

)