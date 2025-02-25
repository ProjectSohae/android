package com.sohae.data.jobinformation.request

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface DistrictRequest {

    // ResponseBody <- raw 파일
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("selectSHBMGHJBCSiGunGu.json")
    fun getDistrictList(
        @Query("hwamyeon_id") hwamyeonId: String = "SHBMBISTCJJeopSuHHAN_M",
        @FieldMap fieldMap: Map<String, String>
    ) : Call<ResponseBody>
}