package com.sohae.feature.jobnavgraph.jobinfolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.domain.jobinformation.usecase.JobInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewJobInfoListViewModel @Inject constructor(
    private val jobInfoUseCase: JobInfoUseCase
): ViewModel() {

    // 접수 년도
    val jeopsu_yy = mapOf(
        Pair("2021년", "2021"),
        Pair("2022년", "2022"),
        Pair("2023년", "2023"),
        Pair("2024년", "2024")
    )

    // 접수 회차
    val jeopsu_tms = mapOf(
        Pair("재학생입영원", "1"),
        Pair("본인선택", "2")
    )

    // 관할 병무청 코드
    val ghjbc_cd = mapOf(
        Pair("서울", "02"),
        Pair("부산•울산", "03"),
        Pair("대구•경북", "04"),
        Pair("경인", "05"),
        Pair("광주•전남", "06"),
        Pair("대전•충남", "07"),
        Pair("강원", "08"),
        Pair("충북", "09"),
        Pair("전북", "10"),
        Pair("경남", "11"),
        Pair("제주", "12"),
        Pair("인천", "13"),
        Pair("경기•북부", "14"),
        Pair( "강원•영동", "15")
    )

    // 시.군.구 주소 코드
    // Pair(sigungu_addr, bjdsggjuso_cd)
    var bjdsggjuso_cd = emptyMap<String, String>()

    val sortByList = listOf(
        "별점 낮은 순",
        "별점 높은 순"
    )

    fun getAddressList(ghjbc_cd: String) {

    }


    fun getBjdsggjusoCd(
        ghjbc_cd: String,
        codegubun: String = "2",
        callback: (String) -> Unit
    ) {
        if (ghjbc_cd.isBlank()) {
            callback("다시 시도해 주세요.")
            return
        }

        bjdsggjuso_cd = emptyMap()

        viewModelScope.launch {
            jobInfoUseCase.getDistrictList(
                ghjbc_cd,
                codegubun,
                callback = { response, errorMessage ->

                    if (response.isNotEmpty()) {
                        bjdsggjuso_cd = response
                    } else {
                        callback(errorMessage)
                    }
                }
            )
        }
    }
}