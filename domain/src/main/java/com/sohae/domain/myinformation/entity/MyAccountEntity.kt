package com.sohae.domain.myinformation.entity

data class MyAccountEntity (

    val id: Int,

    // 실명
    val realName: String,

    // 닉네임
    val nickname: String,

    // 이메일
    val emailAddress: String,
)