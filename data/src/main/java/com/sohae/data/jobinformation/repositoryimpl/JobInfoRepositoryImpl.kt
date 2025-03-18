package com.sohae.data.jobinformation.repositoryimpl

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sohae.common.models.workplace.entity.ApplyHistoryEntity
//import com.sohae.common.models.workplace.entity.CityType
//import com.sohae.common.models.workplace.entity.DistrictType
import com.sohae.data.jobinformation.method.DistrictRequestMethod
import com.sohae.data.jobinformation.method.JobApplyHistoryRequestMethod
import com.sohae.domain.jobinformation.repository.JobInfoRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JobInfoRepositoryImpl: JobInfoRepository {

    private val DISTRICT_URL = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/"
    private val url = "https://mwpt.mma.go.kr/caisBMHS/dmem/dmem/mwgr/shbm/"

    private val client = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(JobApplyHistoryRequestMethod::class.java)

    private val districtClient = Retrofit.Builder()
        .baseUrl(DISTRICT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(DistrictRequestMethod::class.java)


    override fun getDistrictList(
        ghjbc_cd: String,
        codegubun: String,
        callback: (Map<String, String>, Boolean) -> Unit
    ) {
        val success = { response: Map<String, String> ->
            callback(response, true)
        }
        val failure = {
            callback(emptyMap(), false)
        }
        val request = districtClient.getDistrictList(
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

                    if (convertedToJson != null) {
                        convertedToJson.asJsonArray.forEach {
                            result[it.asJsonObject.get("sigungu_addr").asString] =
                                it.asJsonObject.get("bjdsggjuso_cd").asString
                        }

                        success(result)
                    } else {
                        failure()
                    }
                } else {
                    failure()
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                failure()
            }
        } )
    }

    override fun getJobApplyHistoryList(
        jeopsu_yy: String,
        jeopsu_tms: String,
        ghjbc_cd: String,
        bjdsggjuso_cd: String,
        callback: (List<ApplyHistoryEntity>, Boolean) -> Unit
    ) {
        val success = { response: List<ApplyHistoryEntity> ->
            callback(response, true)
        }
        val failure = {
            callback(emptyList(), false)
        }
        val nullString = "해당 없음"
        val request = client.getJobApplyHistoryList(
            jeopsu_yy = jeopsu_yy,
            jeopsu_tms = jeopsu_tms,
            ghjbc_cd = ghjbc_cd,
            bjdsggjuso_cd = bjdsggjuso_cd
        )

        request.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(p0: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.body() != null) {
                    Gson()
                        .fromJson(response.body()!!.string(), JsonObject::class.java)
                        ?.get("sHBokMuMWVOList")
                        ?.asJsonArray?.let { jsonArray ->

                            success(
                                jsonArray.map{ item ->
                                    val jsonObject = item.asJsonObject

                                    if (jsonObject != null) {
                                        ApplyHistoryEntity(
//                                            cityType = CityType.entries.find {
//                                                it.cd == bjdsggjuso_cd
//                                            } ?: CityType.SEOUL,
//                                            districtType = DistrictType.entries.find {
//                                                it.mndCode == bjdsggjuso_cd
//                                            } ?: DistrictType.SEOUL_DONGJAK_GU,
                                            소집_일자 = jsonObject.get("shbmsojip_dt")?.asString ?: nullString,
                                            선복무 = jsonObject.get("seonbokmu_yn")?.asBoolean ?: false,
                                            복무_기관 = jsonObject.get("bokmu_ggm")?.asString ?: nullString,
                                            복무_기관_분류 = jsonObject.get("ssggdbunryu_nm")?.asString ?: nullString,
                                            공석 = jsonObject.get("gsbaejeong_pcnt")?.asInt ?: 0,
                                            선택_인원_1지망 = jsonObject.get("bistinweon")?.asInt ?: 0,
                                            전공자_선택_인원_1지망 = jsonObject.get("jm1stinwon_jeongong")?.asInt ?: 0,
                                            탈락_횟수별_1지망_선택_인원_3회_이상 = jsonObject.get("jm1stinwon_talrak3")?.asInt ?: 0,
                                            탈락_횟수별_1지망_선택_인원_2회 = jsonObject.get("jm1stinwon_talrak2")?.asInt ?: 0,
                                            탈락_횟수별_1지망_선택_인원_1회 = jsonObject.get("jm1stinwon_talrak1")?.asInt ?: 0,
                                            탈락_횟수별_1지망_선택_인원_0회 = jsonObject.get("jm1stinwon_talrak0")?.asInt ?: 0,
                                            선택_인원_2지망 = jsonObject.get("bistinweon2")?.asInt ?: 0,
                                            전공자_선택_인원_2지망 = jsonObject.get("jm2stinwon_jeongong")?.asInt ?: 0,
                                            탈락_횟수별_2지망_선택_인원_3회_이상 = jsonObject.get("jm2stinwon_talrak3")?.asInt ?: 0,
                                            탈락_횟수별_2지망_선택_인원_2회 = jsonObject.get("jm2stinwon_talrak2")?.asInt ?: 0,
                                            탈락_횟수별_2지망_선택_인원_1회 = jsonObject.get("jm2stinwon_talrak1")?.asInt ?: 0,
                                            탈락_횟수별_2지망_선택_인원_0회 = jsonObject.get("jm2stinwon_talrak0")?.asInt ?: 0,
                                            입영_부대 = jsonObject.get("budae_cdm")?.asString ?: nullString,
                                            복무_기관_전화번호 = jsonObject.get("bmgigwan_jhbh")?.asString ?: nullString,
                                            세부_주소 = jsonObject.get("shbjsiseol_sbjs")?.asString ?: nullString,
                                        )
                                    }
                                    else {
                                        ApplyHistoryEntity(
//                                            cityType = CityType.SEOUL,
//                                            districtType = DistrictType.SEOUL_DONGJAK_GU,
                                            소집_일자 = nullString,
                                            선복무 = false,
                                            복무_기관 = nullString,
                                            복무_기관_분류 = nullString,
                                            공석 = 0,
                                            선택_인원_1지망 = 0,
                                            전공자_선택_인원_1지망 = 0,
                                            탈락_횟수별_1지망_선택_인원_3회_이상 = 0,
                                            탈락_횟수별_1지망_선택_인원_2회 = 0,
                                            탈락_횟수별_1지망_선택_인원_1회 = 0,
                                            탈락_횟수별_1지망_선택_인원_0회 = 0,
                                            선택_인원_2지망 = 0,
                                            전공자_선택_인원_2지망 = 0,
                                            탈락_횟수별_2지망_선택_인원_3회_이상 = 0,
                                            탈락_횟수별_2지망_선택_인원_2회 = 0,
                                            탈락_횟수별_2지망_선택_인원_1회 = 0,
                                            탈락_횟수별_2지망_선택_인원_0회 = 0,
                                            입영_부대 = nullString,
                                            복무_기관_전화번호 = nullString,
                                            세부_주소 = nullString,
                                        )
                                    }
                                }.toList()
                            )
                        } ?: {
                            failure()
                    }

                } else {
                    failure()
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                failure()
            }
        })
    }
}