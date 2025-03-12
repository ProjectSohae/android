package com.sohae.domain.signin.repository

import com.sohae.domain.signin.entity.AuthTokenEntity
import com.sohae.domain.signin.type.AuthType

interface SignInRepository {

    fun signIn(
        authType: AuthType,
        socialToken: String,
        callback: (AuthTokenEntity?) -> Unit
    )
}