package com.sohae.domain.profile.repository

import com.sohae.domain.myinformation.entity.MyAccountEntity

interface ProfileReposiotory {

    fun getMyProfile(
        accessToken: String,
        callback: (MyAccountEntity?) -> Unit
    )
}