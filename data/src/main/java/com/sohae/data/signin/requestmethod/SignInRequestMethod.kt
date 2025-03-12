package com.sohae.data.signin.requestmethod

import com.sohae.common.models.auth.request.SocialLoginRequest
import com.sohae.data.signin.response.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SignInRequestMethod {

    @POST("/auth/login/{auth_type}")
    fun signIn(
        @Path("auth_type") authType: String,
        @Body token: SocialLoginRequest
    ): Call<SignInResponse>
}