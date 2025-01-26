package com.example.gongik.view.jobinformation.jobapplyhistory

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gongik.model.data.jobinformation.repository.DistrictRepository
import kotlinx.coroutines.launch

class JobApplyHistoryViewModel: ViewModel() {

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

    val jobDetailsCategory = listOf(
        Pair("소집 일자", 120.dp),
        Pair("분류", 160.dp),
        Pair("복무 기관", 160.dp),
        Pair("최대 스택", 120.dp),
        Pair("최대 스택 경쟁률", 160.dp),
        Pair("전공자", 80.dp),
        Pair("공석", 80.dp),
        Pair("1지망 경쟁률", 120.dp),
        Pair("3회+", 80.dp),
        Pair("2회", 80.dp),
        Pair("1회", 80.dp),
        Pair("0회", 80.dp),
        Pair("훈련소", 120.dp)
    )

    val posts = listOf(
        1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
    )

    fun getBjdsggjusoCd(
        jeopsu_yy: String,
        jeopsu_tms: String,
        ghjbc_cd: String,
        codegubun: String = "2",
        callback: (String) -> Unit
    ) {
        if (
            jeopsu_yy.isBlank()
            || jeopsu_tms.isBlank()
            || ghjbc_cd.isBlank()
        ) {
            callback("다시 시도해 주세요.")
            return
        }

        bjdsggjuso_cd = emptyMap()

        viewModelScope.launch {
            DistrictRepository.getDistrictList(
                jeopsu_yy,
                jeopsu_tms,
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