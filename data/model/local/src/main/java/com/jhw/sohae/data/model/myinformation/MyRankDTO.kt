package com.jhw.sohae.data.model.myinformation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_rank")
data class MyRankDTO (
    @PrimaryKey val id: Int = 0,
    // 일등병 진급 일자
    @ColumnInfo(name = "first_promotion_day") val firstPromotionDay: Long,
    // 상등병 진급 일자
    @ColumnInfo(name = "second_promotion_day") val secondPromotionDay: Long,
    // 병장 진급 일자
    @ColumnInfo(name = "third_promotion_day") val thirdPromotionDay: Long,
)