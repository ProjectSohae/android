package com.sohae.data.jobinformation.request

import com.sohae.data.jobinformation.dto.CoordinateDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeocodingRequest {

    @Headers(
        "x-ncp-apigw-api-key-id: audv8yyyoy",
        "x-ncp-apigw-api-key: cD3FKmJs5isJCtmOuvjiqMcbckMM6BP2Vzpkn4Gf",
        "Accept: application/json"
    )
    @GET
    fun getCoordinate(
        @Query("query") location: String
    ): Call<CoordinateDTO>
}