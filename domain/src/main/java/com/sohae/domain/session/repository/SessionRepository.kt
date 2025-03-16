package com.sohae.domain.session.repository

import com.sohae.domain.session.entity.AuthTokenEntity
import com.sohae.domain.session.type.AuthType

interface SessionRepository {

    fun signIn(
        authType: AuthType,
        socialToken: String,
        callback: (AuthTokenEntity?) -> Unit
    )

    fun signOut(
        callback: (Boolean) -> Unit
    )
}