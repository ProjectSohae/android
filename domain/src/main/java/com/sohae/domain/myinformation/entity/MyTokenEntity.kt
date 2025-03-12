package com.sohae.domain.myinformation.entity

data class MyTokenEntity(

    val id: Int = 0,

    val accessToken: String,

    val refreshToken: String
)
