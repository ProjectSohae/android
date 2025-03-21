package com.sohae.data.profile.requestmethod

import com.sohae.data.profile.response.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

private const val USER_URL = "user"

interface ProfileRequestMethod {

    @GET("${USER_URL}/profile")
    fun getMyProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>
}