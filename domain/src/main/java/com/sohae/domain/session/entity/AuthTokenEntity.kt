package com.sohae.domain.session.entity

data class AuthTokenEntity (

    val accessToken: String,

    val refreshToken: String
)