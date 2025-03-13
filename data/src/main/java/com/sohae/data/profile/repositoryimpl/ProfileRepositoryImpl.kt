package com.sohae.data.profile.repositoryimpl

import android.util.Log
import com.sohae.data.profile.mapper.toUserProfileEntity
import com.sohae.data.profile.requestmethod.ProfileRequestMethod
import com.sohae.data.profile.response.UserProfileResponse
import com.sohae.domain.myinformation.entity.MyAccountEntity
import com.sohae.domain.profile.repository.ProfileReposiotory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
): ProfileReposiotory {

    private val tag = "sohae_profile"

    private val client = retrofit.create(ProfileRequestMethod::class.java)

    override fun getMyProfile(
        accessToken: String,
        callback: (MyAccountEntity?) -> Unit
    ) {
        val success = { response: UserProfileResponse ->
            callback(response.toUserProfileEntity())
        }

        val failure = { errMsg: String, err: Throwable? ->
            Log.e(tag, errMsg, err)
            callback(null)
        }

        val request = client.getMyProfile("Bearer $accessToken")

        request.enqueue(object: Callback<UserProfileResponse>{
            override fun onResponse(
                p0: Call<UserProfileResponse>,
                p1: Response<UserProfileResponse>
            ) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failure("서버 응답 없음", Throwable(p1.errorBody()?.string()))
                    }
                } else {
                    failure("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                }
            }

            override fun onFailure(p0: Call<UserProfileResponse>, p1: Throwable) {
                failure("서버 요청 실패", p1)
            }
        })
    }
}