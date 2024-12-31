package com.example.gongik.model.data.myinformation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_work_information")
data class MyWorkInformation (
    @PrimaryKey val uid: Int = 0,
    // 복무지
    @ColumnInfo(name = "work_place") val workPlace: String,
    // 소집일
    @ColumnInfo(name = "start_work_day") val startWorkDay: Int,
    // 소집 해제일
    @ColumnInfo(name = "finish_work_day") val finishWorkDay: Int,
)