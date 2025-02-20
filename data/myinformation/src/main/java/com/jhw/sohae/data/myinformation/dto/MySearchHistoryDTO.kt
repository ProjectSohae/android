package com.jhw.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_search_history")
data class MySearchHistoryDTO(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "keyword")
    val keyword: String
)
