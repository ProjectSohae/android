package com.sohae.presentation.jobapplyhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sohae.data.jobinformation.repositoryimpl.DistrictRepositoryImpl
import com.sohae.data.jobinformation.repositoryimpl.JobApplyHistoryRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    val jobDetailsFirstCategory = listOf(
        Pair("", 7.5f),
        Pair("1지망 경쟁률", 5.5f),
        Pair("1지망 선택 인원", 5.25f),
        Pair("2지망 경쟁률", 5.5f),
        Pair("2지망 선택 인원", 5.25f),
        Pair("", 7f),
    )

    val jobDetailsSecondCategory = listOf(
        Pair("소집 일자", 1.5f),
        Pair("분류", 2f),
        Pair("복무 기관", 3f),
        Pair("공석", 1f),
        Pair("지원자 최대 스택", 2f),
        Pair("최대 스택 경쟁률", 2f),
        Pair("전체 경쟁률", 1.5f),
        Pair("전공자", 1.25f),
        Pair("3회+", 1f),
        Pair("2회", 1f),
        Pair("1회", 1f),
        Pair("0회", 1f),
        Pair("지원자 최대 스택", 2f),
        Pair("최대 스택 경쟁률", 2f),
        Pair("전체 경쟁률", 1.5f),
        Pair("전공자", 1.25f),
        Pair("3회+", 1f),
        Pair("2회", 1f),
        Pair("1회", 1f),
        Pair("0회", 1f),
        Pair("세부 주소", 5f),
        Pair("훈련소", 2f)
    )

    private var _jobApplyHistoryList = MutableStateFlow(listOf<List<String>>())
    val jobApplyHistoryList = _jobApplyHistoryList.asStateFlow()

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
            DistrictRepositoryImpl.getDistrictList(
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

    fun updateJobApplyHistoryList(input: List<List<String>>) {
        _jobApplyHistoryList.value = input
    }

    fun getJobApplyHistoryList(
        jeopsu_yy: String,
        jeopsu_tms: String,
        ghjbc_cd: String,
        bjdsggjuso_cd: String,
        callback: (String) -> Unit
    ) {
        if (
            jeopsu_yy.isBlank()
            || jeopsu_tms.isBlank()
            || ghjbc_cd.isBlank()
            || bjdsggjuso_cd.isBlank()
        ) {
            callback("입력 값이 잘못되었습니다.")
            return
        }

        viewModelScope.launch {
            val tmp = mutableListOf<List<String>>()
            val getMaxStack: (String, String, String, String) -> Int = { zero, one, two, three ->

                if (three != "0") {
                    3
                } else if (two != "0") {
                    2
                } else if (one != "0") {
                    1
                } else if (zero != "0") {
                    0
                } else {
                    -1
                }
            }
            val maxStackToString: (Int) -> String = { value ->

                when (value) {
                    3 -> { "3스택+" }
                    2 -> { "2스택" }
                    1 -> { "1스택" }
                    0 -> { "0스택" }
                    else -> { "없음" }
                }
            }
            val getMaxStackCount: (Int, String, String, String, String) -> Int = { maxStack, zeroCnt, oneCnt, twoCnt, threeCnt ->

                when (maxStack) {
                    3 -> { threeCnt.toInt() }
                    2 -> { twoCnt.toInt() }
                    1 -> { oneCnt.toInt() }
                    0 -> { zeroCnt.toInt() }
                    else -> { -1 }
                }
            }
            val getMaxStackCompetitionRate: (Int, Int) -> String = { value, div ->
                if (value > 0 && div != 0) {
                    "${value.toFloat() / div.toFloat()}:1"
                } else {
                    "없음"
                }
            }
            val getAllCompetitionRate: (String, String, String, String, String) -> String = { zeroStack, oneStack, twoStack, threeStack, div ->
                val total = zeroStack.toInt() + oneStack.toInt() + twoStack.toInt() + threeStack.toInt()

                if (total > 0 && div.toInt() != 0) {
                    "${total.toFloat() / div.toInt().toFloat()}:1"
                } else {
                    "없음"
                }
            }

            JobApplyHistoryRepositoryImpl.getJobApplyHistoryList(
                jeopsu_yy = jeopsu_yy,
                jeopsu_tms = jeopsu_tms,
                ghjbc_cd = ghjbc_cd,
                bjdsggjuso_cd = bjdsggjuso_cd,
                callback = { jobApplyHistoryList, errorMessage ->

                    if (errorMessage.isBlank()) {
                        jobApplyHistoryList.forEach {

                        }
                        updateJobApplyHistoryList(tmp)
                    } else {
                        callback(errorMessage)
                    }
                }
            )
        }
    }
}