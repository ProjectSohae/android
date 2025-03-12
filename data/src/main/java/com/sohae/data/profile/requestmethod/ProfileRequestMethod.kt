package com.sohae.data.profile.requestmethod

import com.sohae.data.profile.response.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileRequestMethod {

    @GET("/user/profile")
    fun getMyProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>
}