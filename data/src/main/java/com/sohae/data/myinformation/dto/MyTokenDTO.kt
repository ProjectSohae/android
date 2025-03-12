package com.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_token")
data class MyTokenDTO(

    @PrimaryKey(false)
    val id: Int = 0,

    @ColumnInfo("access_token")
    val accessToken: String,

    @ColumnInfo("refresh_token")
    val refreshToken: String
)
