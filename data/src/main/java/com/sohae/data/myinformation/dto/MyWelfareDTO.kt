package com.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_welfare")
data class MyWelfareDTO (
    @PrimaryKey val id: Int = 0,
    // 식비
    @ColumnInfo(name = "lunch_support") var lunchSupport: Int,
    // 교통비
    @ColumnInfo(name = "transportation_support") var transportationSupport: Int,
    // 월급 계산 시작일
    @ColumnInfo(name = "payday") var payday: Int,
)