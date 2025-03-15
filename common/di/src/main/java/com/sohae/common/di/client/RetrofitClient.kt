package com.sohae.common.di.client

import com.sohae.common.di.interceptor.TokenInterceptor
import com.sohae.data.myinformation.repositoryimpl.MyInfoRepositoryImpl
import com.sohae.domain.myinformation.usecase.MyInfoUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val url = "http://192.168.103.2:8080/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitWithToken: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(
                    TokenInterceptor(
                        MyInfoUseCase(MyInfoRepositoryImpl)
                    )
                )
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}