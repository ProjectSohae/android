package com.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_account")
data class MyAccountDTO (
    // 고유 식별자
    @PrimaryKey val idx: Int = 0,
    // uuid
    @ColumnInfo(name = "id") val id: String,
    // 닉네임
    @ColumnInfo(name = "username") val username: String,
    // 이메일
    @ColumnInfo(name = "email") val emailAddress: String,
)