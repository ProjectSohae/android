package com.example.sohae.model.data.myinformation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sohae.R

val leaveKindList: List<Pair<String, Int>> = listOf(
    Pair("1년차 연차", R.drawable.outline_annual_leave_24),
    Pair("2년차 연차", R.drawable.outline_annual_leave_24),
    Pair("병가", R.drawable.outline_plus_bottle_24),
    Pair("기타 휴가", R.drawable.outline_annual_leave_24),
    Pair("복무이탈", R.drawable.baseline_warning_amber_24)
)

val leaveTypeList = listOf(
    listOf( "연차", "오전 반차", "오후 반차", "지각", "조퇴", "외출" ),
    listOf( "연차", "오전 반차", "오후 반차", "지각", "조퇴", "외출" ),
    listOf( "연차", "오전 반차", "오후 반차", "지각", "조퇴", "외출" ),
    listOf( "특별 휴가", "청원 휴가", "공가", "대체 휴일" ),
    listOf( "무단 지각", "무단 조퇴", "무단 외출", "복무지 이탈(무단 결근)" )
)

@Entity(tableName = "my_used_leave")
data class MyUsedLeave (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,

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