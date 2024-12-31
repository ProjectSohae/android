package com.example.gongik.model.data.myinformation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

val leaveType = listOf(
    "특별 휴가",
    "청원 휴가",
    "공가",
    "외출",
    "조퇴",
    "복무 이탈"
)

val leaveTypeIdx = mapOf(
    Pair("특별 휴가", 0),
    Pair("청원 휴가", 1),
    Pair("공가", 2),
    Pair("외출", 3),
    Pair("조퇴", 4),
    Pair("복무 이탈", 5)
)

@Entity(tableName = "my_used_leave")
data class MyUsedLeave (
    @PrimaryKey(autoGenerate = true) val uid: Int,

    // 휴가 종류
    @ColumnInfo("leave_type_idx") val leaveTypeIdx: Int,

    // 휴가 사유
    @ColumnInfo("reason") val reason: String,

    // 휴가 소요 시간, 1시간 단위
    @ColumnInfo("used_leave_time") val usedLeaveTime: Int,

    // 휴가 시작 시일
    @ColumnInfo("start_leave_time") val startLeaveTime: Long,

    // 식비 지급 여부
    @ColumnInfo("is_pay_food_costs") val isPayFoodCosts: Boolean,

    // 교통비 지급 여부
    @ColumnInfo("is_pay_transportation_costs") val isPayTransportationCosts: Boolean,
)