package com.sohae.domain.profile.usecase

import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.profile.repository.ProfileReposiotory
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val profileReposiotory: ProfileReposiotory
) {

    fun getMyProfile(
        accessToken: String,
        callback: (MyAccountEntity?) -> Unit
    ) {
        profileReposiotory.getMyProfile(accessToken) {
            callback(it)
        }
    }
}