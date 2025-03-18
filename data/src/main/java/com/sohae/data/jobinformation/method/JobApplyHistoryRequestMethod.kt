package com.sohae.data.jobinformation.method

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface JobApplyHistoryRequestMethod {

    @POST("selectSHBMBISTBMGGCJJeopSuHH.json")
    fun getJobApplyHistoryList(
        @Query("hwamyeon_id") hwamyeon_id: String = "SHBMBISTCJJeopSuHHAN_M",
        @Query("jeopsu_yy") jeopsu_yy: String,
        @Query("jeopsu_tms") jeopsu_tms: String,
        @Query("ghjbc_cd") ghjbc_cd: String,
        @Query("bjdsggjuso_cd") bjdsggjuso_cd: String
    ): Call<ResponseBody>
}