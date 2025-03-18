package com.sohae.common.di.interceptor

import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class CheckExpiredTokenInterceptor(
    private val myInfoUseCase: MyInfoUseCase
): Interceptor {

    private val BEARER_PREFIX = "Bearer "

    override fun intercept(chain: Interceptor.Chain): Response {

        val accessToken = runBlocking { myInfoUseCase.getMyAccessTokenNotFlow() }

        var request = chain.request().newBuilder()
            .addHeader("Authorization", BEARER_PREFIX + accessToken)
            .build()
        var response = chain.proceed(request)

        if (response.code == 403) {

            response.close()

            val refreshToken = runBlocking { myInfoUseCase.getMyRefreshTokenNotFlow() }

            if (accessToken != null && refreshToken != null) {

                request = request.newBuilder()
                    .addHeader("Refresh-Token", BEARER_PREFIX + refreshToken)
                    .build()
                response = chain.proceed(request)

                val responseAccessToken = response.headers["Access-Token"]?.substring(BEARER_PREFIX.length)
                val responseRefreshToken = response.headers["Refresh-Token"]?.substring(BEARER_PREFIX.length)

                if (responseAccessToken != null && responseRefreshToken != null) {
                    myInfoUseCase.updateMyToken(
                        MyTokenEntity(
                            accessToken = responseAccessToken,
                            refreshToken = responseRefreshToken)
                    )
                }
            }
        }

        return response
    }
}