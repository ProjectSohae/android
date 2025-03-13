package com.sohae.domain.myinformation.entity

import java.util.UUID

data class MyAccountEntity (

    // uuid
    val id: UUID,

    // 닉네임
    val username: String,

    // 이메일
    val emailAddress: String,
)