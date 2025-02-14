package com.jhw.sohae.presentation.jobinformation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

val jobInfotmationDetails = listOf(
    "기관 분류",
    "관할 병무청",
    "등록 지역",
    "상위 기관",
    "주소",
    "선발 제한",
    "성범죄자 제한",
    "전화번호",
)

val jobCompetitionRecordCategory = listOf(
    "소집일자",
    "공석",
    "3회+",
    "2회",
    "1회",
    "0회"
)

enum class JobInformationCategory(
    val idx: Int,
    val categoryName: String
) {
    PROFILE(0, "소개"),
    REVIEW(1, "리뷰"),
}

class JobInformationViewModel: ViewModel() {

}