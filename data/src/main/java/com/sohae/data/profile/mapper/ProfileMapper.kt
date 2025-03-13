package com.sohae.data.profile.mapper

import com.sohae.data.profile.response.UserProfileResponse
import com.sohae.domain.myinformation.entity.MyAccountEntity
import java.util.UUID

fun UserProfileResponse.toUserProfileEntity() = MyAccountEntity(
    id = UUID.fromString(this.id),
    username = this.username,
    emailAddress = this.email
)