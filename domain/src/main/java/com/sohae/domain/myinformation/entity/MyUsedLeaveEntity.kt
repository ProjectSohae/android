package com.sohae.domain.myinformation.entity


data class MyUsedLeaveEntity (

    val id: Int,

    // 휴가 종류
    val leaveKindIdx: Int,

    // 휴가 하위 종류
    val leaveTypeIdx: Int,

    // 휴가 사유
    val reason: String,

    // 휴가 소요 시간, 밀리초 단위
    val usedLeaveTime: Long,

    // 휴가 시작 시일
    val leaveStartDate: Long,

    // 휴가 마지막 시일
    val leaveEndDate: Long,

    // 식비 지급 여부
    val receiveLunchSupport: Boolean,

    // 교통비 지급 여부
    val receiveTransportationSupport: Boolean,
)