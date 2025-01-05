package com.example.gongik.model.data.myinformation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

val ranksList = listOf(
    "이등병",
    "일등병",
    "상등병",
    "병장"
)

@Entity(tableName = "my_rank")
data class MyRank (
    @PrimaryKey val uid: Int = 0,
    // 현재 계급
    // 0: 이등병, 1: 일등병, 2: 상등병, 3: 병장
    @ColumnInfo(name = "current_rank") val currentRank: Int,
    // 일등병 진급 일자
    @ColumnInfo(name = "first_promotion_day") val firstPromotionDay: Timestamp,
    // 상등병 진급 일자
    @ColumnInfo(name = "second_promotion_day") val secondPromotionDay: Timestamp,
    // 병장 진급 일자
    @ColumnInfo(name = "third_promotion_day") val thirdPromotionDay: Timestamp,
)