package com.jhw.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 반가, 조퇴, 외출 등을 시간 단위로 사용할 수 있으며,
// 도합 8시간이 될 시 연차 1일 차감
@Entity(tableName = "my_leave")
data class MyLeaveDTO (
    @PrimaryKey val id: Int = 0,
    // 1년차 연가
    @ColumnInfo(name = "first_annual_leave") val firstAnnualLeave: Int,
    // 2년차 연가
    @ColumnInfo(name = "second_annual_leave") val secondAnnualLeave: Int,
    // 병가
    @ColumnInfo(name = "sick_leave") val sickLeave: Int,
)