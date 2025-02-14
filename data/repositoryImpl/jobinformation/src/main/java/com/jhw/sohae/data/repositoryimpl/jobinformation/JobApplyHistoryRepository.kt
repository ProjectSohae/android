package com.jhw.sohae.data.repositoryimpl.jobinformation

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jhw.sohae.data.model.jobinformation.JobApplyHistory
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
        callback: (List<JobApplyHistory>, String) -> Unit
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
                    val result = mutableListOf<JobApplyHistory>()

                    convertedToJson.let{ jsonObject ->

                        if (jsonObject == null) {
                            callback(emptyList(), "결과가 존재하지 않습니다.")
                        } else {
                            jsonObject.asJsonArray.forEach {
                                result.add(
                                    JobApplyHistory(
                                        shbmsojip_dt = it.asJsonObject.get("shbmsojip_dt").asString,
                                        seonbokmu_yn = it.asJsonObject.get("seonbokmu_yn").asString,
                                        ssggdbunryu_nm = it.asJsonObject.get("ssggdbunryu_nm").asString,
                                        gsbaejeong_pcnt = it.asJsonObject.get("gsbaejeong_pcnt").asString,
                                        bokmu_ggm = it.asJsonObject.get("bokmu_ggm").asString,
                                        bistinweon = it.asJsonObject.get("bistinweon").asString,
                                        jm1stinwon_talrak3 = it.asJsonObject.get("jm1stinwon_talrak3").asString,
                                        jm1stinwon_talrak2 = it.asJsonObject.get("jm1stinwon_talrak2").asString,
                                        jm1stinwon_talrak1 = it.asJsonObject.get("jm1stinwon_talrak1").asString,
                                        jm1stinwon_talrak0 = it.asJsonObject.get("jm1stinwon_talrak0").asString,
                                        jm1stinwon_jeongong = it.asJsonObject.get("jm1stinwon_jeongong").asString,
                                        bistinweon2 = it.asJsonObject.get("bistinweon2").asString,
                                        jm2stinwon_talrak3 = it.asJsonObject.get("jm2stinwon_talrak3").asString,
                                        jm2stinwon_talrak2 = it.asJsonObject.get("jm2stinwon_talrak2").asString,
                                        jm2stinwon_talrak1 = it.asJsonObject.get("jm2stinwon_talrak1").asString,
                                        jm2stinwon_talrak0 = it.asJsonObject.get("jm2stinwon_talrak0").asString,
                                        jm2stinwon_jeongong = it.asJsonObject.get("jm2stinwon_jeongong").asString,
                                        budae_cdm = it.asJsonObject.get("budae_cdm").asString,
                                        bmgigwan_jhbh = it.asJsonObject.get("bmgigwan_jhbh").asString,
                                        shbjsiseol_sbjs = it.asJsonObject.get("shbjsiseol_sbjs").asString
                                    )
                                )
                            }
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