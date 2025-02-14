package com.jhw.sohae.domain.myinformation.entity

data class MyWelfareEntity (

    val id: Int,

    // 식비
    var lunchSupport: Int,

    // 교통비
    var transportationSupport: Int,

    // 월급 계산 시작일
    var payday: Int,
)