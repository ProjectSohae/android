package com.sohae.common.di.client

import com.sohae.common.di.BuildConfig
import com.sohae.common.di.interceptor.CheckExpiredTokenInterceptor
import com.sohae.data.myinformation.repositoryimpl.MyInfoRepositoryImpl
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val URL = BuildConfig.SERVER_IP

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitWithToken: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(
                    CheckExpiredTokenInterceptor(MyInfoUseCase(MyInfoRepositoryImpl))
                )
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}