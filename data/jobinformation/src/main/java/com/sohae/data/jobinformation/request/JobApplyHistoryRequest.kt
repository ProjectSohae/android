package com.sohae.data.jobinformation.request

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface JobApplyHistoryRequest {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("selectSHBMBISTBMGGCJJeopSuHH.json")
    fun getJobApplyHistoryList(
        @Query("hwamyeon_id") hwamyeon_id: String = "SHBMBISTCJJeopSuHHAN_M",
        @FieldMap fieldMap: Map<String, String>
    ): Call<ResponseBody>
}