package com.sohae.data.session.repositoryimpl

import android.util.Log
import com.sohae.common.models.auth.request.SocialLoginRequest
import com.sohae.data.session.requestmethod.SessionRequestMethod
import com.sohae.data.session.response.SignInResponse
import com.sohae.domain.session.entity.AuthTokenEntity
import com.sohae.domain.session.repository.SessionRepository
import com.sohae.domain.session.type.AuthType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit,
    private val retrofitWithToken: Retrofit
): SessionRepository {

    val tag = "sohae_session"

    val client = retrofit.create(SessionRequestMethod::class.java)

    val clientWithToken = retrofitWithToken.create(SessionRequestMethod::class.java)

    override fun signIn(
        authType: AuthType,
        socialToken: String,
        callback: (AuthTokenEntity?) -> Unit
    ) {
        val success = { response: SignInResponse ->
            callback(
                AuthTokenEntity(
                    response.accessToken,
                    response.refreshToken
                )
            )
        }
        val failure = { msg: String, e: Throwable? ->
            Log.e(tag, msg, e)
            callback(null)
        }

        val request = client.signIn(authType.name, SocialLoginRequest(socialToken))

        request.enqueue(object: Callback<SignInResponse> {

            override fun onResponse(p0: Call<SignInResponse>, p1: Response<SignInResponse>) {

                val e = Throwable(p1.errorBody()?.string())

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failure("서버 응답 내용 없음", e)
                    }
                } else {
                    failure("서버 응답 실패", e)
                }
            }

            override fun onFailure(p0: Call<SignInResponse>, p1: Throwable) {
                failure("서버 요청 실패", p1)
            }
        })
    }

    override fun signOut(
        callback: (Boolean) -> Unit
    ) {
        val success = { callback(true) }
        val failure = { msg: String, e: Throwable? ->
            Log.e(tag, msg, e)
            callback(false)
        }
        val request = clientWithToken.singOut()

        request.enqueue(object: Callback<Unit> {
            override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {

                val e = Throwable(p1.errorBody()?.string())

                if (p1.isSuccessful) {
                    success()
                } else {
                    failure("서버 응답 실패", e)
                }
            }

            override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                failure("서버 요청 실패", p1)
            }
        })

    }
}