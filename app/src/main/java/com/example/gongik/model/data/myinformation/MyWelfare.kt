package com.example.gongik.model.data.myinformation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_welfare")
data class MyWelfare (
    @PrimaryKey val uid: Int = 0,
    // 식비
    @ColumnInfo(name = "food_costs") var foodCosts: Int,
    // 교통비
    @ColumnInfo(name = "transportation_costs") var transportationCosts: Int,
    // 월급 계산 시작일
    @ColumnInfo(name = "payday") var payday: Int,
)