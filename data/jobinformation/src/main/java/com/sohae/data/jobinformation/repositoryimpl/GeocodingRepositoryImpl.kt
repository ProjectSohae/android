package com.sohae.data.jobinformation.repositoryimpl

import com.sohae.data.jobinformation.dto.CoordinateDTO
import com.sohae.data.jobinformation.request.GeocodingRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeocodingRepositoryImpl {

    val client = Retrofit.Builder()
        .baseUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GeocodingRequest::class.java)

    fun getCoordinate(
        location: String
    ) {
        val request = client.getCoordinate(location)

        request.enqueue(object: Callback<CoordinateDTO>{
            override fun onResponse(p0: Call<CoordinateDTO>, p1: Response<CoordinateDTO>) {

            }

            override fun onFailure(p0: Call<CoordinateDTO>, p1: Throwable) {

            }
        })
    }
}