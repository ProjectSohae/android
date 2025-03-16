package com.sohae.domain.session.usecase

import com.sohae.domain.session.entity.AuthTokenEntity
import com.sohae.domain.session.repository.SessionRepository
import com.sohae.domain.session.type.AuthType
import javax.inject.Inject

class SessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {

    fun getAuthToken(
        authType: AuthType,
        socialToken: String,
        callback: (AuthTokenEntity?) -> Unit
    ) {

        sessionRepository.signIn(authType, socialToken) { authTokenEntity ->
            authTokenEntity?.let(callback)
        }
    }

    fun signOut(
        callback: (Boolean) -> Unit
    ) {
        sessionRepository.signOut { isSucceed ->
            callback(isSucceed)
        }
    }
}