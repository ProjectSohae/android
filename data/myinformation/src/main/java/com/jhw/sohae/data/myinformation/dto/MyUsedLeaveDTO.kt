package com.jhw.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_used_leave")
data class MyUsedLeaveDTO (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // 휴가 종류
    @ColumnInfo("leave_kind_idx") val leaveKindIdx: Int,

    // 휴가 하위 종류
    @ColumnInfo("leave_type_idx") val leaveTypeIdx: Int,

    // 휴가 사유
    @ColumnInfo("reason") val reason: String,

    // 휴가 소요 시간, 밀리초 단위
    @ColumnInfo("used_leave_time") val usedLeaveTime: Long,

    // 휴가 시작 시일
    @ColumnInfo("leave_start_date") val leaveStartDate: Long,

    // 휴가 마지막 시일
    @ColumnInfo("leave_end_date") val leaveEndDate: Long,

    // 식비 지급 여부
    @ColumnInfo("receive_lunch_support") val receiveLunchSupport: Boolean,

    // 교통비 지급 여부
    @ColumnInfo("receive_transportation_support") val receiveTransportationSupport: Boolean,
)