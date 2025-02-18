package com.jhw.sohae.data.jobinformation

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sohae.common.models.workplace.entity.ApplyHistoryEntity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface JobApplyHistoryRequests {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("selectSHBMBISTBMGGCJJeopSuHH.json")
    fun getJobApplyHistoryList(
        @Query("hwamyeon_id") hwamyeon_id: String = "SHBMBISTCJJeopSuHHAN_M",
        @FieldMap fieldMap: Map<String, String>
    ): Call<ResponseBody>
}

object JobApplyHistoryRepository {

    private const val url = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/"

    private val client = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(JobApplyHistoryRequests::class.java)

    fun getJobApplyHistoryList(
        jeopsu_yy: String,
        jeopsu_tms: String,
        ghjbc_cd: String,
        bjdsggjuso_cd: String,
        callback: (List<ApplyHistoryEntity>, String) -> Unit
    ) {
        val request = client.getJobApplyHistoryList(
            fieldMap = mapOf(
                Pair("jeopsu_yy", jeopsu_yy),
                Pair("jeopsu_tms", jeopsu_tms),
                Pair("ghjbc_cd", ghjbc_cd),
                Pair("bjdsggjuso_cd", bjdsggjuso_cd)
            )
        )

        request.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(p0: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.body() != null) {
                    val convertedToJson = Gson()
                        .fromJson(response.body()!!.string(), JsonObject::class.java)
                        .get("sHBokMuMWVOList")
                    val result = mutableListOf<ApplyHistoryEntity>()

                    convertedToJson.let{ jsonObject ->

                        if (jsonObject == null) {
                            callback(emptyList(), "결과가 존재하지 않습니다.")
                        } else {

                        }
                    }

                    callback(result, "")
                } else {
                    callback(emptyList(), "결과가 존재하지 않습니다.")
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                callback(emptyList(), "목록을 불러 오는데 실패했습니다.")
            }
        })
    }
}