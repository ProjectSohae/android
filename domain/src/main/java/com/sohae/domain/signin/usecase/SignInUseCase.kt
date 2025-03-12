package com.sohae.domain.signin.usecase

import com.sohae.domain.signin.entity.AuthTokenEntity
import com.sohae.domain.signin.repository.SignInRepository
import com.sohae.domain.signin.type.AuthType
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {

    fun getAuthToken(
        authType: AuthType,
        socialToken: String,
        callback: (AuthTokenEntity?) -> Unit
    ) {

        signInRepository.signIn(authType, socialToken) { authTokenEntity ->
            authTokenEntity?.let(callback)
        }
    }
}