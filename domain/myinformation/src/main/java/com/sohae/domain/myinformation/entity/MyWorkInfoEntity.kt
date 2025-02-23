package com.sohae.domain.myinformation.entity

data class MyWorkInfoEntity (

    val id: Int,

    // 복무지
    val workPlace: String,

    // 소집일
    val startWorkDay: Long,

    // 소집 해제일
    val finishWorkDay: Long,

)