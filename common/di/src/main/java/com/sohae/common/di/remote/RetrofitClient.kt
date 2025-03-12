package com.sohae.common.di.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.103.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}