package com.sohae.data.signin.repositoryimpl

import android.util.Log
import com.sohae.common.models.auth.request.SocialLoginRequest
import com.sohae.data.signin.requestmethod.SignInRequestMethod
import com.sohae.data.signin.response.SignInResponse
import com.sohae.domain.myinformation.entity.MyTokenEntity
import com.sohae.domain.signin.entity.AuthTokenEntity
import com.sohae.domain.signin.repository.SignInRepository
import com.sohae.domain.signin.type.AuthType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
): com.sohae.domain.signin.repository.SignInRepository {

    val tag = "sohae_signIn"

    val client = retrofit.create(SignInRequestMethod::class.java)

    override fun signIn(
        authType: com.sohae.domain.signin.type.AuthType,
        socialToken: String,
        callback: (com.sohae.domain.signin.entity.AuthTokenEntity?) -> Unit
    ) {
        val success = { response: SignInResponse ->
            callback(
                com.sohae.domain.signin.entity.AuthTokenEntity(
                    response.accessToken,
                    response.refreshToken
                )
            )
        }
        val failure = { callback(null) }

        val request = client.signIn(authType.name, SocialLoginRequest(socialToken))

        request.enqueue(object: Callback<SignInResponse> {

            override fun onResponse(p0: Call<SignInResponse>, p1: Response<SignInResponse>) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        Log.e(tag, "서버 응답 내용 없음")
                        failure()
                    }
                } else {
                    Log.e(tag, "서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<SignInResponse>, p1: Throwable) {
                Log.e(tag, "서버 요청 실패", p1)
                failure()
            }
        })
    }
}