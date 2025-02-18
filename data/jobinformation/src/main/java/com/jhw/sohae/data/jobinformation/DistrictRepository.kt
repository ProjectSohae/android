package com.jhw.sohae.data.jobinformation

import com.google.gson.Gson
import com.google.gson.JsonObject
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

interface DistrictRequests {

    // ResponseBody <- raw 파일
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("selectSHBMGHJBCSiGunGu.json")
    fun getDistrictList(
        @Query("hwamyeon_id") hwamyeonId: String = "SHBMBISTCJJeopSuHHAN_M",
        @FieldMap fieldMap: Map<String, String>
    ) : Call<ResponseBody>
}

object DistrictRepository {

    private const val DISTRICT_URL = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/"

    private val client: DistrictRequests = Retrofit.Builder()
        .baseUrl(DISTRICT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(DistrictRequests::class.java)

    fun getDistrictList(
        ghjbc_cd: String,
        codegubun: String,
        callback: (Map<String, String>, String) -> Unit
    ) {
        val request = client.getDistrictList(
            fieldMap = mapOf(
                Pair("ghjbc_cd", ghjbc_cd),
                Pair("codegubun", codegubun)
            )
        )

        request.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(req: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.body() != null) {
                    val convertedToJson = Gson()
                        .fromJson(response.body()!!.string(), JsonObject::class.java)
                        .get("codeList")
                    val result = mutableMapOf<String, String>()

                    convertedToJson.asJsonArray.forEach {
                        result.put(
                            it.asJsonObject.get("sigungu_addr").asString,
                            it.asJsonObject.get("bjdsggjuso_cd").asString
                        )
                    }

                    callback(result, "")
                } else {
                    callback(emptyMap(), "결과가 존재하지 않습니다.")
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                callback(emptyMap(), "목록을 불러 오는데 실패했습니다.")
            }
        } )
    }
}