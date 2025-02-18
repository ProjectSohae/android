package com.jhw.sohae.data.myinformation.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_account")
data class MyAccountDTO (
    // 고유 식별자
    @PrimaryKey val id: Int = 0,
    // 본명
    @ColumnInfo(name = "real_name") val realName: String,
    // 닉네임
    @ColumnInfo(name = "nickname") val nickname: String,
    // 이메일
    @ColumnInfo(name = "email_address") val emailAddress: String,
)