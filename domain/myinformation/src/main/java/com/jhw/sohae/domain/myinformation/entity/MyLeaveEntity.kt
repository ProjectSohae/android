package com.jhw.sohae.domain.myinformation.entity

// 반가, 조퇴, 외출 등을 시간 단위로 사용할 수 있으며,
// 도합 8시간이 될 시 연차 1일 차감
data class MyLeaveEntity (

    val id: Int,

    // 1년차 연가
    val firstAnnualLeave: Int,

    // 2년차 연가
    val secondAnnualLeave: Int,

    // 병가
    val sickLeave: Int,

)