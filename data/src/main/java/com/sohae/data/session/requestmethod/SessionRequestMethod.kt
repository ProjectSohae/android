package com.sohae.data.session.requestmethod

import com.sohae.common.models.auth.request.SocialLoginRequest
import com.sohae.data.session.response.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

private const val AUTH_URL = "auth"

interface SessionRequestMethod {

    @POST("${AUTH_URL}/login/{auth_type}")
    fun signIn(
        @Path("auth_type") authType: String,
        @Body token: SocialLoginRequest
    ): Call<SignInResponse>

    @POST("${AUTH_URL}/logout")
    fun singOut(): Call<Unit>
}